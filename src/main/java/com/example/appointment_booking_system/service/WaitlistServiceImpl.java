package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.entity.Doctor;
import com.example.appointment_booking_system.entity.User;
import com.example.appointment_booking_system.entity.Waitlist;
import com.example.appointment_booking_system.repository.DoctorRepository;
import com.example.appointment_booking_system.repository.UserRepository;
import com.example.appointment_booking_system.repository.WaitlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WaitlistServiceImpl implements WaitlistService {

    @Autowired
    private WaitlistRepository waitlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ 1. Inject your real EmailService
    @Autowired
    private EmailService emailService;

    @Override
    public String joinWaitlist(Long userId, Long doctorId, LocalDate date) {

        // 1. Prevent Duplicates
        boolean alreadyWaiting = waitlistRepository.existsByUserIdAndDoctorIdAndAppointmentDateAndStatus(
                userId, doctorId, date, "WAITING"
        );

        if (alreadyWaiting) {
            return "You are already on the waitlist for this date!";
        }

        // 2. Fetch Entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 3. Save to Database
        Waitlist newEntry = new Waitlist(user, doctor, date);
        waitlistRepository.save(newEntry);

        return "Success! You have been added to the waitlist.";
    }

    @Override
    @Transactional // Ensures the status update only happens if the logic succeeds
    public void checkWaitlistAfterCancellation(Long doctorId, LocalDate date) {

        System.out.println("Checking waitlist for Doctor ID: " + doctorId + " on " + date);

        // 1. Find the first person in line (First Come, First Served)
        Optional<Waitlist> nextPatient = waitlistRepository
                .findFirstByDoctorIdAndAppointmentDateAndStatusOrderByCreatedAtAsc(
                        doctorId, date, "WAITING"
                );

        if (nextPatient.isPresent()) {
            Waitlist entry = nextPatient.get();
            User user = entry.getUser();
            String doctorName = entry.getDoctor().getName();

            // 2. Prepare Email Details
            String subject = "🔔 Slot Open! Dr. " + doctorName + " is available.";
            String body = "Hello " + user.getName() + ",\n\n" +
                    "Good news! A slot has just opened up for Dr. " + doctorName + " on " + date + ".\n" +
                    "Please log in to the application immediately to book your appointment before someone else does.\n\n" +
                    "Best Regards,\nAppointment Booking Team";

            // 3. ✅ Send Real Email (Using the method from your EmailService)
            // This runs in the background if you added @Async to EmailService
            emailService.sendEmail(user.getEmail(), subject, body);

            // 4. Update Status in Database
            entry.setStatus("NOTIFIED");
            waitlistRepository.save(entry);

            System.out.println("✅ Real notification sent to: " + user.getEmail());

        } else {
            System.out.println("No one is waiting for this slot.");
        }
    }
}