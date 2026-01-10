package com.example.appointment_booking_system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RescheduleRequestDTO {

    private LocalDate newDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
