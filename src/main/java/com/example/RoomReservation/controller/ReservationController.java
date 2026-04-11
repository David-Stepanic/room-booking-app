package com.example.RoomReservation.controller;

import com.example.RoomReservation.service.ReservationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService reservationService) {
        this.service = reservationService;
    }

}
