package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.entity.Appointment;
import com.example.appointment_booking_system.entity.AppointmentStatus;
import com.example.appointment_booking_system.entity.CancelledAppointment;
import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.repository.AppointmentRepository;
import com.example.appointment_booking_system.repository.CancelledAppointmentRepository;
import com.example.appointment_booking_system.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final CancelledAppointmentRepository cancelledRepo;

    public DoctorAppointmentServiceImpl(
            AppointmentRepository appointmentRepo,
            DoctorRepository doctorRepo,
            CancelledAppointmentRepository cancelledRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.cancelledRepo = cancelledRepo;
    }

    // Add this inside DoctorAppointmentServiceImpl.java

    @Override
    public List<AppointmentResponseDTO> getPatientHistoryForDoctor(Long userId, String doctorEmail) {
        Doctor doctor = doctorRepo.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // This calls your repository to find all COMPLETED appointments for this user/doctor
        return appointmentRepo.findByUserIdAndDoctorIdAndStatus(
                        userId, doctor.getId(), AppointmentStatus.COMPLETED)
                .stream()
                .map(this::mapToDTO) // This uses your FIXED mapToDTO with userId
                .toList();
    }





    @Override
    public List<AppointmentResponseDTO> getTodayAppointments(String email) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        LocalDate today = LocalDate.now();
        return appointmentRepo.findByDoctorIdAndAppointmentDate(doctor.getId(), today)
                .stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDate(String email, LocalDate date) {
        Doctor doctor = doctorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return appointmentRepo.findByDoctorIdAndAppointmentDate(doctor.getId(), date)
                .stream().map(this::mapToDTO).toList();
    }

    @Override
    public void markCompleted(Long id, String prescriptionNotes) {
        Appointment a = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        a.setStatus(AppointmentStatus.COMPLETED);
        a.setPrescription(prescriptionNotes);
        appointmentRepo.save(a);
    }

    @Override
    public void markCompleted(Long id) {
        markCompleted(id, "No notes provided.");
    }

    @Override
    public void cancel(Long id, String reason) {
        Appointment a = appointmentRepo.findById(id)
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
        appointmentRepo.delete(a);
    }

    // ✅ FIXED HELPER METHOD
    private AppointmentResponseDTO mapToDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .appointmentId(a.getId())        // From appointments.id
                .userId(a.getUser().getId())     // From appointments.user_id -> user.id
                .userName(a.getUser().getName()) // From user.name
                .doctorId(a.getDoctor().getId()) // From appointments.doctor_id -> doctor.id
                .doctorName(a.getDoctor().getName()) // From doctor.name
                .doctorSpecialization(a.getDoctor().getSpecialization()) // From doctor.specialization
                .date(a.getAppointmentDate())    // From appointments.appointment_date
                .startTime(a.getStartTime())     // From appointments.start_time
                .endTime(a.getEndTime())         // From appointments.end_time
                .status(a.getStatus().name())    // From appointments.status
                .ticketId(a.getTicketId())       // From appointments.ticket_id
                .prescription(a.getPrescription()) // From appointments.prescription
                .build();
    }


}