📌 Overview

This project is a Hospital Management System Backend Application developed using Spring Boot, designed to digitally connect patients and doctors through a secure, scalable, and real-world appointment booking system.

In traditional hospital workflows, patients often face manual token systems, long queues, and inefficient appointment handling.
This system eliminates manual processes by enabling patients to book appointments online, receive QR-based tickets, and get real-time notifications via Email and SMS.

🎯 Core Objective

Replace manual hospital token systems

Reduce patient waiting time

Improve doctor-patient coordination

Ensure secure, role-based access

Provide a real-world hospital workflow simulation

👥 System Roles

The application is built using role-based architecture with three primary entities:

🔑 Admin

Manages doctors

Controls system-level operations

Ensures doctor authenticity

🩺 Doctor

Sets availability

Validates patient tickets

Manages consultations

Adds prescriptions

Views patient history

👤 User (Patient)

Registers and logs in securely

Books appointments

Receives tickets & notifications

Views prescriptions and history

🔐 Security & Authentication

Implemented JWT (JSON Web Token) Authentication

Secure login & registration

Role-based authorization (Admin / Doctor / User)

Protected REST APIs using Spring Security

Token-based stateless authentication

👤 User (Patient) Features

User registration & login

JWT-secured authentication

User dashboard

View available doctors

Book appointments based on doctor availability

Automatic QR-based ticket generation

Appointment confirmation via Email & SMS

Join waiting list when slots are full

Automatic ticket generation when a slot becomes available

View current & previous prescriptions

Access complete appointment history

🩺 Doctor Features

Doctor dashboard

Set available appointment slots

View scheduled appointments

Scan and validate QR tickets

Doctor-specific ticket validation

Conduct consultations

Mark appointments as completed

Add digital prescriptions

View patient medical history

Access previous prescriptions for better diagnosis

⚠️ Doctors cannot self-register.
Doctors are added only by the Admin to maintain security and authenticity.

🛠️ Admin Features

Admin authentication

Add doctors to the system

Manage doctor records

Control system-level operations

Prevent unauthorized doctor creation

📧 Notification System

Email Service Integration

SMS Service Integration

Appointment confirmation notifications

Waiting list notifications

Event-driven notifications on appointment cancellation

🔄 Waiting List Mechanism

If all slots are booked:

User can join a waiting list

If an appointment is cancelled:

Event is triggered automatically

Next user in waiting list is notified

Ticket is generated

Email & SMS are sent instantly

📷 QR Code Ticket Validation System

Each appointment generates a unique QR code linked to:

User ID

Doctor ID

Appointment slot

✔ Valid Case

User books appointment with Doctor A

User visits Doctor A

QR scanned → Ticket validated successfully

❌ Invalid Case

User books appointment with Doctor 1

User visits Doctor 2

QR scanned → Validation fails

Error shown:

❌ Invalid Ticket
This ticket belongs to Doctor 1


This ensures:

Doctor-specific authorization

Appointment integrity

Zero ticket misuse

📝 Consultation & Prescription Flow

Doctor scans and verifies QR ticket

Consultation is performed

Doctor marks appointment as Completed

Doctor adds a digital prescription

Prescription is:

Stored securely in database

Visible in user dashboard

Added to patient medical history

📚 Patient Medical History

Doctors can view complete patient history

Includes:

Previous appointments

Past prescriptions

Consultation details

Helps in accurate diagnosis and continuity of care

🔄 End-to-End Appointment Lifecycle

User registers & logs in

Doctor sets availability

User books appointment

QR-based ticket generated

Email & SMS sent

Doctor scans QR

Doctor-specific validation

Consultation completed

Prescription added

Appointment marked completed

Prescription visible in user dashboard

History stored for future visits

🧩 Tech Stack

Backend: Spring Boot

Security: Spring Security + JWT

Database: MySQL / PostgreSQL (configurable)

Email: Spring Mail

SMS: SMS Gateway Integration

QR Code: QR-based ticket validation

Build Tool: Maven

Architecture: RESTful APIs

📌 Real-World Impact

Eliminates manual hospital queues

Improves patient experience

Secure and scalable architecture

Doctor-specific appointment enforcement

Digital prescription & history management

Real-time notification system

🚀 Future Enhancements

Payment integration

Video consultations

Frontend integration (React / Angular)

Analytics dashboard for admin

Medical reports upload

Mobile application support

👨‍💻 Author

Developed by: Arun Kumar 
Role: Backend Developer – Spring Boot
Focus: Secure, real-world healthcare solutions
