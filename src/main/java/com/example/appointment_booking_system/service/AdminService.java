package com.example.appointment_booking_system.service;

//package com.example.appointment.service;



import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.dto.CreateDoctorAccountDTO;

import java.util.List;

public interface AdminService {

    List<AppointmentResponseDTO> getAllAppointments();

    List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId);

    void cancelAppointmentByAdmin(Long appointmentId);
    void createDoctorAccount(CreateDoctorAccountDTO dto);
}

