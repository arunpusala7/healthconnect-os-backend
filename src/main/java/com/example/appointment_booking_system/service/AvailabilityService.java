package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AvailabilityRequestDTO;
import com.example.appointment_booking_system.dto.DoctorAvailabilityDTO;

import java.time.LocalDate;

public interface AvailabilityService {

    DoctorAvailabilityDTO getDoctorAvailability(
            Long doctorId,
            LocalDate date,
            Long excludeAppointmentId
    );


    // Doctor adds availability
    void addAvailabilityForDoctor(
            AvailabilityRequestDTO dto,
            String email
    );
}
