package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.entity.CancelledAppointment;
import com.example.appointment_booking_system.repository.CancelledAppointmentRepository;
import com.example.appointment_booking_system.service.DoctorAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
//@CrossOrigin(origins = "http://localhost:5173")
public class DoctorAppointmentController {

    private final DoctorAppointmentService service;

    // ✅ NEW: Inject Repository to view history
    private final CancelledAppointmentRepository cancelledRepo;

    public DoctorAppointmentController(DoctorAppointmentService service,
                                       CancelledAppointmentRepository cancelledRepo) {
        this.service = service;
        this.cancelledRepo = cancelledRepo;
    }

    // 1️⃣ View appointments (Today or Specific Date)
    @GetMapping("/appointments")
    public List<AppointmentResponseDTO> getAppointments(
            Authentication auth,
            @RequestParam(required = false) LocalDate date) {

        if (date == null) date = LocalDate.now();
        return service.getAppointmentsByDate(auth.getName(), date);
    }

    // Kept for backward compatibility
    @GetMapping("/appointments/today")
    public List<AppointmentResponseDTO> todayAppointments(Authentication auth) {
        return service.getTodayAppointments(auth.getName());
    }

    // 2️⃣ Mark Completed (With Prescription)
    @PutMapping("/appointments/{id}/complete")
    public String complete(
            @PathVariable Long id,
            @RequestBody(required = false) String prescriptionNotes) {

        if (prescriptionNotes != null && !prescriptionNotes.isEmpty()) {
            service.markCompleted(id, prescriptionNotes);
        } else {
            service.markCompleted(id, "No notes provided.");
        }

        return "Appointment marked as COMPLETED";
    }

    // ✅ 3️⃣ UPDATED: Cancel with Reason
    @PutMapping("/appointments/{id}/cancel")
    public String cancel(
            @PathVariable Long id,
            @RequestBody(required = false) String reason) {

        // If no reason sent, set a default
        String finalReason = (reason != null && !reason.trim().isEmpty()) ? reason : "Doctor cancelled without specific reason";

        service.cancel(id, finalReason);
        return "Appointment cancelled and slot freed.";
    }

    // ✅ 4️⃣ NEW: View Cancelled History
    @GetMapping("/appointments/cancelled")
    public List<CancelledAppointment> getCancelledAppointments(Authentication auth) {
        return cancelledRepo.findByDoctorEmail(auth.getName());
    }
    // Endpoint: GET /api/doctor/patient-history/5
    @GetMapping("/patient-history/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientHistory(
            @PathVariable Long userId,
            Authentication authentication) {

        // Get logged-in doctor's email
        String doctorEmail = authentication.getName();

        List<AppointmentResponseDTO> history = service.getPatientHistoryForDoctor(userId, doctorEmail);
        return ResponseEntity.ok(history);
    }
}