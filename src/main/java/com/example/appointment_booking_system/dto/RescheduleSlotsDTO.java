package com.example.appointment_booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RescheduleSlotsDTO {

    private Long doctorId;
    private LocalDate date;
    private List<Slot> slots;

    @Data
    @AllArgsConstructor
    public static class Slot {
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
