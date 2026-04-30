package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import java.util.List;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest request, String email);

    List<ReservationResponse> getAllReservations();

    ReservationResponse confirmReservation(Long id);

    ReservationResponse cancelReservation(Long id);

    ReservationResponse declineReservation(Long id, String declineReason);

    void deleteReservation(Long id);
}
