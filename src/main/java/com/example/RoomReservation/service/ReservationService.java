package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.model.Reservation;

public interface ReservationService{
    ReservationResponse createReservation(ReservationRequest request);
}
