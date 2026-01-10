package com.example.appointment_booking_system.repository;

import com.example.appointment_booking_system.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    // 1. THE CORE METHOD: Finds the next person in line
    // Logic: Match Doctor + Match Date + Match Status -> Sort by Oldest First -> Take Top 1
    Optional<Waitlist> findFirstByDoctorIdAndAppointmentDateAndStatusOrderByCreatedAtAsc(
            Long doctorId,
            LocalDate appointmentDate,
            String status
    );

    // 2. DUPLICATE CHECK: Prevents a user from joining the same waitlist twice
    boolean existsByUserIdAndDoctorIdAndAppointmentDateAndStatus(
            Long userId,
            Long doctorId,
            LocalDate appointmentDate,
            String status
    );

    // 3. CLEANUP: Find all entries for a specific date (e.g., to clear expired ones)
    long countByDoctorIdAndAppointmentDateAndStatus(
            Long doctorId,
            LocalDate appointmentDate,
            String status
    );
}