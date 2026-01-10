package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.dto.UserProfileDTO;
import com.example.appointment_booking_system.entity.User; // Assuming your entity is named User
import com.example.appointment_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDTO getMyProfile(String email) {
        // Find user by email (from JWT)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert Entity to DTO
        return new UserProfileDTO(
                user.getName(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}