/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.david.RoomReservation.model;

import com.david.RoomReservation.exception.custom.RoomException;
import com.david.RoomReservation.model.constans.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roomNumber;
    private int capacity;
    @Setter
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", capacity=" + capacity +
                ", roomType=" + roomType +
                ", reservations=" + reservations +
                '}';
    }

    public Room(int roomNumber, int capacity, RoomType roomType, List<Reservation> reservations) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.reservations = reservations;
    }

    public void updateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new RoomException("Capacity must be greater than 0!");
        }
        this.capacity = capacity;
    }

    public void updateRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new RoomException("Room number must be positive!");
        }
        this.roomNumber = roomNumber;
    }

}
