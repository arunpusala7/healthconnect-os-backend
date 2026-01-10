package com.example.appointment_booking_system.exception;

//package com.example.appointment.exception;

public class SlotAlreadyBookedException extends RuntimeException {

    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}

