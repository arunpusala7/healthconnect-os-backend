package com.example.appointment_booking_system.dto;

public class LoginResponseDTO {

    private final String token;
    private final String role;
    private final Long userId;  // ← NEW

    public LoginResponseDTO(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }
}