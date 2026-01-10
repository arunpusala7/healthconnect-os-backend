package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.RescheduleRequestDTO;
import com.example.appointment_booking_system.dto.RescheduleSlotsDTO;
import com.example.appointment_booking_system.entity.Appointment;
import com.example.appointment_booking_system.entity.Availability;
import com.example.appointment_booking_system.repository.AppointmentRepository;
import com.example.appointment_booking_system.repository.AvailabilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RescheduleServiceImpl implements RescheduleService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;

    public RescheduleServiceImpl(
            AppointmentRepository appointmentRepository,
            AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // 🔹 FETCH SLOTS FOR RESCHEDULE
    @Override
    public RescheduleSlotsDTO getSlotsForReschedule(
            Long appointmentId,
            Long doctorId,
            LocalDate date) {

        Appointment existing = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        List<Availability> availabilityList =
                availabilityRepository.findByDoctorIdAndDate(doctorId, date);

        List<Appointment> booked =
                appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        List<RescheduleSlotsDTO.Slot> slots = new ArrayList<>();

        for (Availability a : availabilityList) {
            LocalTime start = a.getStartTime();
            LocalTime end = a.getEndTime();
            int mins = a.getSlotDurationMinutes();

            while (!start.plusMinutes(mins).isAfter(end)) {
                LocalTime slotEnd = start.plusMinutes(mins);
                LocalTime slotStart = start;

                boolean bookedAlready = booked.stream()
                        .filter(ap -> !ap.getId().equals(existing.getId())) // 🔥 ignore current appt
                        .anyMatch(ap -> ap.getStartTime().equals(slotStart));

                if (!bookedAlready) {
                    slots.add(new RescheduleSlotsDTO.Slot(slotStart, slotEnd));
                }

                start = slotEnd;
            }
        }

        return new RescheduleSlotsDTO(doctorId, date, slots);
    }

    // 🔹 RESCHEDULE
    @Override
    public void rescheduleAppointment(
            Long appointmentId,
            RescheduleRequestDTO dto) {

        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setAppointmentDate(dto.getNewDate());
        appt.setStartTime(dto.getStartTime());
        appt.setEndTime(dto.getEndTime());
        appt.setStatus(appt.getStatus().RESCHEDULED);

        appointmentRepository.save(appt);
    }


}
