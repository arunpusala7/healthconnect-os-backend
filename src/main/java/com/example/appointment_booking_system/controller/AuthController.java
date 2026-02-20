package com.example.appointment_booking_system.controller;

import com.example.appointment_booking_system.dto.LoginRequestDTO;
import com.example.appointment_booking_system.dto.LoginResponseDTO;
import com.example.appointment_booking_system.dto.RegisterRequestDTO;
import com.example.appointment_booking_system.entity.Role;
import com.example.appointment_booking_system.entity.User;
import com.example.appointment_booking_system.security.CustomUserDetails;
import com.example.appointment_booking_system.security.JwtTokenProvider;
import com.example.appointment_booking_system.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ===============================
    // REGISTER USER / ADMIN
    // ===============================
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDTO requestDTO) {

        Role role = Role.valueOf(requestDTO.getRole().toUpperCase());

        userService.registerUser(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                role
        );

        return "User registered successfully";
    }


    // ===============================
    // LOGIN
    // ===============================
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(userDetails);

        // ← CHANGE ONLY THIS LINE
        return new LoginResponseDTO(
                token,
                userDetails.getRole().name(),
                userDetails.getUser().getId()  // ← This gets the actual user ID from the entity
        );
    }
}
