package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AvailabilityRequestDTO;
import com.example.appointment_booking_system.dto.DoctorAvailabilityDTO;
import com.example.appointment_booking_system.entity.Appointment;
import com.example.appointment_booking_system.entity.Availability;
import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.repository.AppointmentRepository;
import com.example.appointment_booking_system.repository.AvailabilityRepository;
import com.example.appointment_booking_system.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AvailabilityServiceImpl(
            AvailabilityRepository availabilityRepository,
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository) {

        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    // =========================================================
    // USER: VIEW DOCTOR AVAILABILITY (BOOKING + RESCHEDULE)
    // =========================================================
    @Override
    public DoctorAvailabilityDTO getDoctorAvailability(
            Long doctorId,
            LocalDate date,
            Long excludeAppointmentId) {

        List<Availability> availabilityList =
                availabilityRepository.findByDoctorIdAndDate(doctorId, date);

        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        List<DoctorAvailabilityDTO.TimeSlot> availableSlots = new ArrayList<>();

        for (Availability availability : availabilityList) {

            LocalTime slotStart = availability.getStartTime();
            LocalTime endTime = availability.getEndTime();
            int slotMinutes = availability.getSlotDurationMinutes();

            while (!slotStart.plusMinutes(slotMinutes).isAfter(endTime)) {

                LocalTime slotEnd = slotStart.plusMinutes(slotMinutes);
                LocalTime finalSlotStart = slotStart;

                boolean isBooked = bookedAppointments.stream()
                        .filter(a -> excludeAppointmentId == null
                                || !a.getId().equals(excludeAppointmentId)) // 🔥 KEY FIX
                        .anyMatch(a -> a.getStartTime().equals(finalSlotStart));

                if (!isBooked) {
                    availableSlots.add(
                            new DoctorAvailabilityDTO.TimeSlot(slotStart, slotEnd)
                    );
                }

                slotStart = slotEnd;
            }
        }

        return DoctorAvailabilityDTO.builder()
                .doctorId(doctorId)
                .date(date)
                .availableSlots(availableSlots)
                .build();
    }


    // =========================================================
    // DOCTOR: ADD AVAILABILITY
    // =========================================================
    @Override
    public void addAvailabilityForDoctor(
            AvailabilityRequestDTO dto,
            String email) {

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (dto.getDate() == null ||
                dto.getStartTime() == null ||
                dto.getEndTime() == null) {
            throw new RuntimeException("Date, start time and end time are required");
        }

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        boolean exists = availabilityRepository
                .existsByDoctorAndDateAndStartTimeAndEndTime(
                        doctor,
                        dto.getDate(),
                        dto.getStartTime(),
                        dto.getEndTime()
                );

        if (exists) {
            throw new RuntimeException("Availability already exists");
        }

        Availability availability = new Availability();
        availability.setDoctor(doctor);
        availability.setDate(dto.getDate());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setSlotDurationMinutes(30);

        availabilityRepository.save(availability);
    }
}
