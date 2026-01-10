package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AppointmentRequestDTO;
import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.entity.*;
import com.example.appointment_booking_system.event.AppointmentBookedEvent;
import com.example.appointment_booking_system.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CancelledAppointmentRepository cancelledRepo;
    private final WaitlistService waitlistService;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  UserRepository userRepository,
                                  DoctorRepository doctorRepository,
                                  ApplicationEventPublisher eventPublisher,
                                  CancelledAppointmentRepository cancelledRepo,
                                  WaitlistService waitlistService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.eventPublisher = eventPublisher;
        this.cancelledRepo = cancelledRepo;
        this.waitlistService = waitlistService;
    }

    // ============================================================
    // ✅ 1. GET DOCTOR APPOINTMENTS (FIXED FOR FRONTEND)
    // ============================================================
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(String email, LocalDate date) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctor.getId(), date);

        return appointments.stream().map(a -> {
            AppointmentResponseDTO dto = new AppointmentResponseDTO();
            dto.setAppointmentId(a.getId());

            // 🔥 EXPLICIT MAPPING: This ensures userId is NOT null
            if (a.getUser() != null) {
                dto.setUserId(a.getUser().getId());
                dto.setUserName(a.getUser().getName());
            }

            dto.setDoctorId(doctor.getId());
            dto.setDoctorName(doctor.getName());
            dto.setDoctorSpecialization(doctor.getSpecialization());
            dto.setDate(a.getAppointmentDate());
            dto.setStartTime(a.getStartTime());
            dto.setEndTime(a.getEndTime());
            dto.setStatus(a.getStatus().name());
            dto.setTicketId(a.getTicketId());
            dto.setPrescription(a.getPrescription());

            return dto;
        }).collect(Collectors.toList());
    }

    // ============================================================
    // ✅ 2. PATIENT HISTORY FOR DOCTOR
    // ============================================================


    // ============================================================
    // ✅ 3. BOOK APPOINTMENT
    // ============================================================
    @Override
    @Transactional
    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO requestDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        boolean slotExists = appointmentRepository
                .existsByDoctorAndAppointmentDateAndStartTimeAndStatusNot(
                        doctor, requestDTO.getDate(), requestDTO.getStartTime(), AppointmentStatus.CANCELLED
                );

        if (slotExists) throw new RuntimeException("Slot already booked");

        Appointment appointment = Appointment.builder()
                .user(user)
                .doctor(doctor)
                .appointmentDate(requestDTO.getDate())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .status(AppointmentStatus.BOOKED)
                .ticketId(UUID.randomUUID().toString())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        eventPublisher.publishEvent(new AppointmentBookedEvent(this, saved));

        return AppointmentResponseDTO.builder()
                .appointmentId(saved.getId())
                .userId(user.getId())
                .userName(user.getName())
                .doctorId(doctor.getId())
                .doctorName(doctor.getName())
                .date(saved.getAppointmentDate())
                .startTime(saved.getStartTime())
                .status(saved.getStatus().name())
                .ticketId(saved.getTicketId())
                .build();
    }

    // ============================================================
    // ✅ 4. CANCEL APPOINTMENT
    // ============================================================
    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        CancelledAppointment cancelled = CancelledAppointment.builder()
                .originalAppointmentId(a.getId())
                .patientName(a.getUser().getName())
                .doctorName(a.getDoctor().getName())
                .doctorEmail(a.getDoctor().getEmail())
                .appointmentDate(a.getAppointmentDate())
                .startTime(a.getStartTime())
                .cancellationReason(reason)
                .cancelledOn(LocalDate.now())
                .build();

        cancelledRepo.save(cancelled);
        appointmentRepository.delete(a);
        waitlistService.checkWaitlistAfterCancellation(a.getDoctor().getId(), a.getAppointmentDate());
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        cancelAppointment(appointmentId, "Cancelled by User");
    }

    // ============================================================
    // ✅ 5. RESCHEDULE APPOINTMENT
    // ============================================================
    @Override
    @Transactional
    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, AppointmentRequestDTO requestDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        LocalDate oldDate = appointment.getAppointmentDate();
        Long doctorId = appointment.getDoctor().getId();

        boolean slotExists = appointmentRepository
                .existsByDoctorAndAppointmentDateAndStartTimeAndStatusNot(
                        appointment.getDoctor(), requestDTO.getDate(), requestDTO.getStartTime(), AppointmentStatus.CANCELLED
                );

        if (slotExists) throw new RuntimeException("New slot already booked");

        appointment.setAppointmentDate(requestDTO.getDate());
        appointment.setStartTime(requestDTO.getStartTime());
        appointment.setEndTime(requestDTO.getEndTime());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        waitlistService.checkWaitlistAfterCancellation(doctorId, oldDate);

        return AppointmentResponseDTO.builder()
                .appointmentId(saved.getId())
                .userId(saved.getUser().getId())
                .userName(saved.getUser().getName())
                .date(saved.getAppointmentDate())
                .status(saved.getStatus().name())
                .build();
    }

    // ============================================================
    // ✅ 6. USER SIDE: VIEW MY APPOINTMENTS
    // ============================================================
    // ============================================================
    // ✅ 6. USER SIDE: VIEW MY APPOINTMENTS (FIXED)
    // ============================================================
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return appointmentRepository.findByUserId(user.getId()).stream()
                .map(a -> {
                    AppointmentResponseDTO dto = new AppointmentResponseDTO();
                    dto.setAppointmentId(a.getId());
                    dto.setUserId(user.getId());
                    dto.setUserName(user.getName());

                    // 🔥 THE FIX: Added doctorId mapping here
                    if (a.getDoctor() != null) {
                        dto.setDoctorId(a.getDoctor().getId());
                        dto.setDoctorName(a.getDoctor().getName());
                        dto.setDoctorSpecialization(a.getDoctor().getSpecialization());
                    }

                    dto.setDate(a.getAppointmentDate());
                    dto.setStartTime(a.getStartTime());
                    dto.setEndTime(a.getEndTime()); // Added for completeness
                    dto.setStatus(a.getStatus().name());
                    dto.setPrescription(a.getPrescription());
                    dto.setTicketId(a.getTicketId()); // Added for prescription downloads
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ============================================================
    // ✅ 7. COMPLETION LOGIC
    // ============================================================
    @Override
    @Transactional
    public AppointmentResponseDTO verifyAndCompleteAppointment(String ticketId) {
        Appointment appointment = appointmentRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Invalid Ticket."));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentResponseDTO.builder()
                .appointmentId(saved.getId())
                .userId(saved.getUser().getId())
                .userName(saved.getUser().getName())
                .status("COMPLETED")
                .build();
    }

    @Override
    @Transactional
    public void verifyAndCompleteAppointmentWithNotes(Long id, String notes) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setStatus(AppointmentStatus.COMPLETED);
        appt.setPrescription(notes);
        appointmentRepository.save(appt);
    }













    // Inside AppointmentServiceImpl.java






}