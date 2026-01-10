package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.AppointmentResponseDTO;
import com.example.appointment_booking_system.dto.CreateDoctorAccountDTO;
import com.example.appointment_booking_system.entity.*;
import com.example.appointment_booking_system.exception.ResourceNotFoundException;
import com.example.appointment_booking_system.repository.AppointmentRepository;
import com.example.appointment_booking_system.repository.DoctorRepository;
import com.example.appointment_booking_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AppointmentRepository appointmentRepository,
                            DoctorRepository doctorRepository,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===============================
    // GET ALL APPOINTMENTS
    // ===============================
    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // GET APPOINTMENTS BY DOCTOR
    // ===============================
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId) {

        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // CANCEL APPOINTMENT (ADMIN)
    // ===============================
    @Override
    public void cancelAppointmentByAdmin(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    // ===============================
    // 🆕 CREATE DOCTOR LOGIN ACCOUNT
    // ===============================
    @Override
    public void createDoctorAccount(CreateDoctorAccountDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Doctor account already exists");
        }

        // ===============================
        // 1️⃣ CREATE USER (LOGIN)
        // ===============================
        User doctorUser = new User();
        doctorUser.setName(dto.getName());
        doctorUser.setEmail(dto.getEmail());
        doctorUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctorUser.setRole(Role.DOCTOR);

        userRepository.save(doctorUser);

        // ===============================
        // 2️⃣ CREATE DOCTOR (DOMAIN ENTITY)
        // ===============================
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialization(dto.getSpecialization()); // ✅ THIS WAS MISSING

        doctorRepository.save(doctor);
    }



    // ===============================
    // MAPPER METHOD
    // ===============================
    private AppointmentResponseDTO mapToDTO(Appointment appointment) {

        return AppointmentResponseDTO.builder()
                .appointmentId(appointment.getId())
                .doctorName(appointment.getDoctor().getName())
                .userName(appointment.getUser().getName())
                .date(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus().name())
                .build();
    }
}
