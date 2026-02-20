package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.UserProfileDTO;
import com.example.appointment_booking_system.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public UserProfileDTO getMyProfile(Authentication authentication) {
        // Get the email from the logged-in user's token
        String email = authentication.getName();

        return userProfileService.getMyProfile(email);
    }
}