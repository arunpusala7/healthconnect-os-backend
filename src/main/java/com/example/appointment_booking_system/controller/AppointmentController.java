package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.AppointmentRequestDTO;
import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ CHANGED: Allow ALL origins ("*") so your HTML Scanner file can connect
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ===============================
    // BOOK APPOINTMENT
    // ===============================
    @PostMapping("/book")
    public AppointmentResponseDTO bookAppointment(
            @RequestBody AppointmentRequestDTO requestDTO,
            Authentication authentication) {

        String email = authentication.getName(); // JWT subject
        return appointmentService.bookAppointment(requestDTO, email);
    }

    // ===============================
    // CANCEL APPOINTMENT
    // ===============================
    @DeleteMapping("/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return "Appointment cancelled successfully";
    }

    // ===============================
    // RESCHEDULE APPOINTMENT
    // ===============================
    @PutMapping("/{appointmentId}/reschedule")
    public AppointmentResponseDTO rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequestDTO requestDTO) {

        return appointmentService.rescheduleAppointment(appointmentId, requestDTO);
    }

    // ===============================
    // GET LOGGED-IN USER APPOINTMENTS
    // ===============================
    @GetMapping("/my")
    public List<AppointmentResponseDTO> getMyAppointments(Authentication authentication) {
        String email = authentication.getName(); // comes from JWT
        return appointmentService.getAppointmentsByUser(email);
    }

    // ===============================
    // ✅ NEW: VERIFY (CHECK-IN) ENDPOINT
    // ===============================
    @PostMapping("/verify/{ticketId}")
    public ResponseEntity<?> verifyAppointment(@PathVariable String ticketId) {
        try {
            // Call service to verify and complete
            AppointmentResponseDTO response = appointmentService.verifyAndCompleteAppointment(ticketId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Return 400 Bad Request if ticket is invalid or already used
            // We wrap the message in a simple object so the frontend JSON parser handles it easily
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Helper class for clean error JSON
    static class ErrorResponse {
        public String message;
        public ErrorResponse(String message) { this.message = message; }
    }
    // Inside AppointmentController.java (User Side)

    @PutMapping("/{id}/cancel")
    public String cancelAppointment(
            @PathVariable Long id,
            @RequestBody String reason) { // ✅ Accept Body

        appointmentService.cancelAppointment(id, reason);
        return "Appointment cancelled successfully.";
    }



}