package com.example.appointment_booking_system.listener;

import com.example.appointment_booking_system.entity.Appointment;
import com.example.appointment_booking_system.event.AppointmentBookedEvent;
import com.example.appointment_booking_system.service.EmailService;
import com.example.appointment_booking_system.service.PdfGeneratorService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {

    private final EmailService emailService;
    private final PdfGeneratorService pdfGeneratorService;

    public AppointmentEventListener(EmailService emailService, PdfGeneratorService pdfGeneratorService) {
        this.emailService = emailService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Async
    @EventListener
    public void handleAppointmentBookedEvent(AppointmentBookedEvent event) {
        Appointment appointment = event.getAppointment();

        System.out.println("⚡ Event received! Generating PDF...");

        // 1. Generate PDF
        byte[] pdfTicket = pdfGeneratorService.generateAppointmentTicket(appointment);

        // 2. Send Email with Attachment
        emailService.sendEmailWithAttachment(
                appointment.getUser().getEmail(),
                "Appointment Confirmation - " + appointment.getTicketId(),
                "Hello " + appointment.getUser().getName() + ",\n\nYour appointment is confirmed! Please find your ticket attached.",
                pdfTicket
        );
    }
}