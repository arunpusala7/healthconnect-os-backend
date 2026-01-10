package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AppointmentResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorAppointmentService {

    List<AppointmentResponseDTO> getTodayAppointments(String doctorEmail);

    // Old method (Keep for safety or backward compatibility)
    void markCompleted(Long appointmentId);

    //void cancel(Long appointmentId);

    List<AppointmentResponseDTO> getAppointmentsByDate(String email, LocalDate date);


    // ✅ NEW METHOD: Accepts prescription notes
    void markCompleted(Long id, String prescriptionNotes);
    void cancel(Long id, String reason);List<AppointmentResponseDTO> getPatientHistoryForDoctor(Long userId, String doctorEmail);
}

