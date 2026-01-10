package com.example.appointment_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // <--- ADD THIS
public class AppointmentBookingSystemApplication {

	public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");

        SpringApplication.run(AppointmentBookingSystemApplication.class, args);
	}

}
