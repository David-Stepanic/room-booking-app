package com.example.RoomReservation.exception.custom;

public class ReservationExistsException extends RuntimeException {
    public ReservationExistsException(String message) {
        super(message);
    }
}
