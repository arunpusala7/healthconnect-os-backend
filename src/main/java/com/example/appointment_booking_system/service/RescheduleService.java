package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.RescheduleRequestDTO;
import com.example.appointment_booking_system.dto.RescheduleSlotsDTO;

import java.time.LocalDate;

public interface RescheduleService {

    RescheduleSlotsDTO getSlotsForReschedule(
            Long appointmentId,
            Long doctorId,
            LocalDate date
    );

    void rescheduleAppointment(
            Long appointmentId,
            RescheduleRequestDTO dto
    );
}
