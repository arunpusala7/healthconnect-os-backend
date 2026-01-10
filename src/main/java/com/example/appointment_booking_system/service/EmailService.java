package com.example.appointment_booking_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired; // Added
import org.springframework.beans.factory.annotation.Value; // Optional: read from properties
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async; // ✅ Import this
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    // It's good practice to read the email from application.properties so you don't hardcode it
    // @Value("${spring.mail.username}")
    // private String fromEmail;
    // Or just keep your hardcoded string for now:
    private final String fromEmail = "arunkumarpusala7@gmail.com";

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // ✅ ADD @Async HERE
    // This allows the waitlist logic to finish instantly while email sends in background
    @Async
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromEmail); // Ensure this matches application.properties

            javaMailSender.send(message);
            System.out.println("✅ (Async) Email Sent Successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
        }
    }

    // Your existing attachment method (can also be Async if you want)
    @Async
    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] pdfBytes) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment("Appointment_Ticket.pdf", new ByteArrayResource(pdfBytes));

            javaMailSender.send(message);
            System.out.println("📧 (Async) PDF Email sent to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send PDF email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}