package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AppointmentRequestDTO;
import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO bookAppointment(AppointmentRequestDTO requestDTO, String email);

    void cancelAppointment(Long appointmentId);

    void cancelAppointment(Long appointmentId, String reason);

    AppointmentResponseDTO rescheduleAppointment(Long appointmentId, AppointmentRequestDTO requestDTO);

    List<AppointmentResponseDTO> getAppointmentsByUser(String email);

    AppointmentResponseDTO verifyAndCompleteAppointment(String ticketId);

    // ✅ ADDED THIS METHOD TO THE INTERFACE
    List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(String email, LocalDate date);


    void verifyAndCompleteAppointmentWithNotes(Long id, String notes);









}