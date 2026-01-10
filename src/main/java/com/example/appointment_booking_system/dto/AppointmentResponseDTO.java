package com.example.appointment_booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long appointmentId;

    // ✅ THIS IS THE MISSING FIELD THAT CAUSES THE ERROR
    private Long doctorId;

    private String userName;
    private String doctorName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String ticketId;
    // ✅ ADD THIS FIELD
    private String prescription;

    // ✅ ADD THIS
    private String doctorSpecialization;

    private Long userId;        // ✅ ADD THIS: Needed to fetch history
}