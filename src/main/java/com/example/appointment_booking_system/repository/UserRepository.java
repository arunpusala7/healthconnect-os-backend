package com.example.appointment_booking_system.repository;

import com.example.appointment_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 Used by Spring Security during login
    Optional<User> findByEmail(String email);

    // (Optional but useful later)
    boolean existsByEmail(String email);
}
