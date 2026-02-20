package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.service.WaitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/waitlist")
//@CrossOrigin(origins = "*") // Allows React/HTML to access this
public class WaitlistController {

    @Autowired
    private WaitlistService waitlistService;

    // ==========================================
    // 1. JOIN THE WAITLIST
    // ==========================================
    // User clicks "Notify Me when available"
    // Usage: POST http://localhost:8080/api/waitlist/join?userId=5&doctorId=2&date=2025-10-25
    @PostMapping("/join")
    public ResponseEntity<String> joinWaitlist(
            @RequestParam Long userId,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String result = waitlistService.joinWaitlist(userId, doctorId, date);

        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            // Return 400 Bad Request if they are already on the list
            return ResponseEntity.badRequest().body(result);
        }
    }

    // ==========================================
    // 2. CHECK STATUS (Optional but helpful)
    // ==========================================
    // Frontend can call this to hide the button if they are already waiting
    // Usage: GET http://localhost:8080/api/waitlist/status?userId=5&doctorId=2&date=2025-10-25
    @GetMapping("/status")
    public ResponseEntity<String> checkStatus(
            @RequestParam Long userId,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        // You would need a method in Service like 'isUserWaiting(...)'
        // For now, we can skip this or implement if you want the UI to be smart.
        return ResponseEntity.ok("Status check feature coming soon.");
    }
}