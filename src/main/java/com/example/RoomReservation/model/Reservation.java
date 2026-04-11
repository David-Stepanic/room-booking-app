/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.RoomReservation.model;

import com.example.RoomReservation.model.constans.ReservationStatus;
import com.example.RoomReservation.model.constans.ReservationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author PC
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String purpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationType reservationType;
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime decisionMadeAt;
    private boolean isApproved;
    private String declinedReason;
    @ManyToOne
    private User user;
    @ManyToOne
    private Room room;

    public Reservation(String name, String purpose, boolean isApproved, User user, Room room) {
        this.name = name;
        this.purpose = purpose;
        this.isApproved = isApproved;
        this.user = user;
        this.room = room;
    }

}
