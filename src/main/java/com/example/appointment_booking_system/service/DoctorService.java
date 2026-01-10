package com.example.appointment_booking_system.service;

//package com.example.appointment.service;



import com.example.appointment_booking_system.entity.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor createDoctor(Doctor doctor);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long id);
    Doctor getDoctorByEmail(String email);

}
