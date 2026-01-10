package com.example.appointment_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cancelled_appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelledAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We store the original ID just for reference (optional)
    private Long originalAppointmentId;

    private String patientName;
    private String doctorName;
    private String doctorEmail; // To filter for the doctor's dashboard

    private LocalDate appointmentDate;
    private LocalTime startTime;

    @Column(length = 500)
    private String cancellationReason;

    private LocalDate cancelledOn; // Date when it was cancelled
}