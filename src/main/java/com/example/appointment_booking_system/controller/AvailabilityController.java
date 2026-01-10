package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.AvailabilityRequestDTO;
import com.example.appointment_booking_system.dto.DoctorAvailabilityDTO;
import com.example.appointment_booking_system.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/availability")
@CrossOrigin(origins = "http://localhost:5173")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // ✅ USER: VIEW DOCTOR AVAILABILITY (BOOK + RESCHEDULE)
    @GetMapping("/doctor/{doctorId}")
    public DoctorAvailabilityDTO getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long excludeAppointmentId
    ) {
        return availabilityService
                .getDoctorAvailability(doctorId, date, excludeAppointmentId);
    }


    // ✅ DOCTOR: ADD AVAILABILITY
    @PostMapping("/add")
    public String addAvailability(
            @RequestBody AvailabilityRequestDTO dto,
            Authentication auth) {

        availabilityService.addAvailabilityForDoctor(dto, auth.getName());
        return "Availability added successfully";
    }
}
