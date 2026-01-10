package com.example.appointment_booking_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Connects to your User entity (The patient)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Connects to your Doctor entity (The doctor they want)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // The specific date the user wants an appointment for
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    // WAITING, NOTIFIED, BOOKED, EXPIRED
    @Column(nullable = false)
    private String status = "WAITING";

    // Timestamp to ensure First-Come-First-Served
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Constructors ---

    // Default constructor (Required by JPA)
    public Waitlist() {
    }

    // Convenience constructor for creating a new entry
    public Waitlist(User user, Doctor doctor, LocalDate appointmentDate) {
        this.user = user;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.status = "WAITING";
    }

    // --- Lifecycle Callbacks ---

    // Automatically sets the timestamp when you save the object
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}