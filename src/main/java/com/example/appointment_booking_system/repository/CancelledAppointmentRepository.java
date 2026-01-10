package com.example.appointment_booking_system.repository;

import com.example.appointment_booking_system.entity.CancelledAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CancelledAppointmentRepository extends JpaRepository<CancelledAppointment, Long> {
    // To show the doctor their cancelled list
    List<CancelledAppointment> findByDoctorEmail(String doctorEmail);
}