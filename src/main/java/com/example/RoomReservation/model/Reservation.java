/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.RoomReservation.model;

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

}
