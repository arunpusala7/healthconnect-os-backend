package com.example.appointment_booking_system.service;

import java.time.LocalDate;

public interface WaitlistService {

    // User wants to join the waitlist
    String joinWaitlist(Long userId, Long doctorId, LocalDate date);

    // System checks waitlist after a cancellation
    void checkWaitlistAfterCancellation(Long doctorId, LocalDate date);
}