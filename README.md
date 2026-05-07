# Room Reservation System

A full-stack Spring Boot application for managing room reservations at a faculty.

## 📌 Description

This application allows users to view available rooms, create reservations, and manage booking time slots. It includes user authentication and basic role-based access control.

## 🚀 Features

- User registration and login
- Room management (create, view, delete)
- Room reservation system
- Reservation time validation
- Basic user-role structure

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## 🗄 Database

- PostgreSQL database: `room-reservations`
- Schema managed via Liquibase migrations
- Seed data included for testing

---

## 👤 Test Users

Email: user@test.com
Password: user

### 🔐 Admin

Email: admin@test.com
Password: admin

## ▶️ How to Run

1. Clone the repository
2. Configure database in `application.properties`
3. Run the application:

```bash
mvn spring-boot:run
