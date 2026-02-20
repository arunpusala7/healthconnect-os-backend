package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.entity.Appointment;
import com.example.appointment_booking_system.repository.AppointmentRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // ✅ IMPORT THIS
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/validate")
//@CrossOrigin(origins = "http://localhost:5173")
public class ValidationController {

    private final AppointmentRepository appointmentRepository;

    public ValidationController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/{ticketId}")
    public Map<String, Object> validateTicket(@PathVariable String ticketId, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        // 1. Check if Doctor is logged in
        if (principal == null) {
            response.put("status", "UNAUTHORIZED");
            return response;
        }

        String loggedInDoctorEmail = principal.getName(); // Get email from Token

        // 2. Find Appointment
        Optional<Appointment> appt = appointmentRepository.findByTicketId(ticketId);

        if (appt.isPresent()) {
            Appointment a = appt.get();
            String appointmentDoctorEmail = a.getDoctor().getEmail();

            // 3. 🔒 SECURITY CHECK: Does this appointment belong to the logged-in doctor?
            if (appointmentDoctorEmail.equals(loggedInDoctorEmail)) {
                // ✅ MATCH: It is YOUR patient
                response.put("status", "VALID");
                response.put("patientName", a.getUser().getName());
                response.put("date", a.getAppointmentDate().toString());
                response.put("time", a.getStartTime().toString());
            } else {
                // ❌ MISMATCH: It is ANOTHER doctor's patient
                response.put("status", "WRONG_DOCTOR");
                response.put("message", "This ticket belongs to Dr. " + a.getDoctor().getName());
            }

        } else {
            response.put("status", "INVALID");
        }

        return response;
    }
}