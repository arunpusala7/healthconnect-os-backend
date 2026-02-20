package com.example.appointment_booking_system.controller;

//package com.example.appointment.controller;


import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.dto.CreateDoctorAccountDTO;
import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ===============================
    // GET ALL APPOINTMENTS
    // ===============================
    @GetMapping("/appointments")
    public List<AppointmentResponseDTO> getAllAppointments() {
        return adminService.getAllAppointments();
    }

    // ===============================
    // GET APPOINTMENTS BY DOCTOR
    // ===============================
    @GetMapping("/appointments/doctor/{doctorId}")
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return adminService.getAppointmentsByDoctor(doctorId);
    }

    // ===============================
    // CANCEL ANY APPOINTMENT
    // ===============================
    @DeleteMapping("/appointments/{appointmentId}")
    public String cancelAppointmentByAdmin(
            @PathVariable Long appointmentId) {

        adminService.cancelAppointmentByAdmin(appointmentId);
        return "Appointment cancelled by admin";
    }

    @PostMapping("/create-doctor-account")
    public String createDoctorAccount(
            @RequestBody CreateDoctorAccountDTO dto) {

        adminService.createDoctorAccount(dto);
        return "Doctor account created successfully";
    }

}

