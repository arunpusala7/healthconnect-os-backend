package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.entity.Role;

public interface UserService {

    void registerUser(String name, String email, String password, Role role);
}
