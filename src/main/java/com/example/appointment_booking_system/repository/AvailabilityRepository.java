package com.example.appointment_booking_system.repository;

//package com.example.appointment.repository;


import com.example.appointment_booking_system.entity.Availability;
import com.example.appointment_booking_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    boolean existsByDoctorAndDateAndStartTimeAndEndTime(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );
}

