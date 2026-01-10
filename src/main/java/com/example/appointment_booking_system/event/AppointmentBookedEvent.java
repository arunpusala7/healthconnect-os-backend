package com.example.appointment_booking_system.event;

import com.example.appointment_booking_system.entity.Appointment;
import org.springframework.context.ApplicationEvent;

public class AppointmentBookedEvent extends ApplicationEvent {

    private final Appointment appointment;

    // ✅ Constructor with NO phone number
    public AppointmentBookedEvent(Object source, Appointment appointment) {
        super(source);
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}