package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.model.Reservation;
import com.example.RoomReservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService reservationService) {
        this.service = reservationService;
    }

    @PostMapping("/create")
    public ReservationResponse createReservation(@RequestBody ReservationRequest request) {
        return service.createReservation(request);
    }

}
