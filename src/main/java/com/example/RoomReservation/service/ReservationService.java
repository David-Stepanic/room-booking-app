package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request, String username);
    List<ReservationResponse> getAllReservations();
    void confirmReservation(Long id);
    void cancelReservation(Long id);
    void declineReservation(Long id);
    void deleteReservation(Long id);
}
