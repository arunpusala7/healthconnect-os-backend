package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.RescheduleRequestDTO;
import com.example.appointment_booking_system.dto.RescheduleSlotsDTO;
import com.example.appointment_booking_system.service.RescheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reschedule")
@CrossOrigin(origins = "http://localhost:5173")
public class RescheduleController {

    private final RescheduleService service;

    public RescheduleController(RescheduleService service) {
        this.service = service;
    }

    // 1️⃣ GET AVAILABLE SLOTS (FOR RESCHEDULE ONLY)
    @GetMapping("/slots")
    public RescheduleSlotsDTO getSlots(
            @RequestParam Long appointmentId,
            @RequestParam Long doctorId,
            @RequestParam LocalDate date) {

        return service.getSlotsForReschedule(
                appointmentId,
                doctorId,
                date
        );
    }

    // 2️⃣ CONFIRM RESCHEDULE
    @PutMapping("/{appointmentId}")
    public String reschedule(
            @PathVariable Long appointmentId,
            @RequestBody RescheduleRequestDTO dto) {

        service.rescheduleAppointment(appointmentId, dto);
        return "Appointment rescheduled successfully";
    }
}
