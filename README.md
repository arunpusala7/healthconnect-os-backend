🏥 Hospital Management System | Appointment Booking System– Backend (Spring Boot)
📌 Overview

A Spring Boot–based Hospital Management System that digitizes the traditional hospital token process by enabling online appointment booking, QR-based ticket validation, and real-time Email notifications.
The system securely connects patients, doctors, and administrators using JWT-based authentication.
Backend-only repository. Frontend code is maintained separately: https://github.com/arunpusala7/healthconnect-os-frontend.git

🎯 Key Goals

Eliminate manual hospital token systems

Reduce patient waiting time

Enable secure doctor–patient coordination

Simulate real-world hospital workflows

👥 Roles

Admin – Adds and manages doctors

Doctor – Sets availability, validates tickets, adds prescriptions

User (Patient) – Books appointments, views tickets & prescriptions

🔐 Security

JWT Authentication

Role-based authorization

Secured REST APIs with Spring Security

✨ Core Features

Online appointment booking

Doctor availability management

QR-code based ticket generation & validation

Doctor-specific ticket enforcement

Email notifications

Waiting list with automatic email notification

Digital prescription management

Patient medical history tracking

📷 QR Ticket Validation

Each ticket is linked to User + Doctor 

Visiting the wrong doctor results in:

❌ Invalid Ticket
This ticket belongs to Doctor 1

📝 Consultation Flow

Doctor scans QR ticket

Ticket is validated

Consultation completed

Doctor adds prescription

Prescription visible in user dashboard

History stored for future visits

🧩 Tech Stack

Backend: Spring Boot

Security: Spring Security + JWT

Database: MySQL / PostgreSQL

Email: Spring Mail

Build Tool: Maven

🚀 Future Enhancements

Payment integration

Video consultations

Admin analytics dashboard

👨‍💻 Author

Arun Kumar
Backend Developer – Spring Boot
