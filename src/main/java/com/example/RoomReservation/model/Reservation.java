/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.RoomReservation.model;

import com.example.RoomReservation.exception.custom.InvalidDateRangeException;
import com.example.RoomReservation.exception.custom.ReservationException;
import com.example.RoomReservation.model.constans.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author PC
 */

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String purpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime decisionMadeAt;
    private String declinedReason;
    @ManyToOne
    private User user;
    @ManyToOne
    private Room room;

    public Reservation() {
        this.reservationStatus = ReservationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.reservationStatus != ReservationStatus.PENDING) {
            throw new ReservationException("Only pending reservations can be confirmed!");
        }
        this.reservationStatus = ReservationStatus.CONFIRMED;
        this.decisionMadeAt = LocalDateTime.now();
    }

    public void decline(String reason) {
        if (this.reservationStatus != ReservationStatus.PENDING) {
            throw new ReservationException("Only pending reservations can be declined!");
        }
        this.reservationStatus = ReservationStatus.DECLINED;
        this.decisionMadeAt = LocalDateTime.now();
        this.declinedReason = reason;
    }

    public void cancel() {
        if (this.reservationStatus != ReservationStatus.PENDING) {
            throw new ReservationException("Only pending reservations can be canceled!");
        }
        this.reservationStatus = ReservationStatus.CANCELED;
        this.decisionMadeAt = LocalDateTime.now();
    }


    public void schedule(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(endTime))
            throw new InvalidDateRangeException("Start time must be before end time");
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDateRangeException("Start time cannot be in the past");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }
}
