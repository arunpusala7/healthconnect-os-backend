package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.service.DoctorService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
//@CrossOrigin(origins = "http://localhost:5173")
public class DoctorPublicController {

    private final DoctorService doctorService;

    public DoctorPublicController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // USER: get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    @GetMapping("/landing-page-doctors")
    public List<Doctor> getAllDoctorsForLandingPage(){
        return doctorService.getAllDoctors();
    }
}

