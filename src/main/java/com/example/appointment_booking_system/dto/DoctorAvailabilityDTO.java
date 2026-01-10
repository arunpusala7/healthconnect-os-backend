package com.example.appointment_booking_system.dto;

//package com.example.appointment.dto;
//package com.example.appointment.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailabilityDTO {

    private Long doctorId;
    private LocalDate date;
    private List<TimeSlot> availableSlots;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;
    }
}


