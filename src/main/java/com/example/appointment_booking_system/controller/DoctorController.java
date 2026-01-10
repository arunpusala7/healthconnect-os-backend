package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.service.AppointmentService;
import com.example.appointment_booking_system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    private final DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // DOCTOR: own profile
    @GetMapping("/me")
    public Doctor getLoggedInDoctor(Authentication auth) {
        return doctorService.getDoctorByEmail(auth.getName());
    }




}

