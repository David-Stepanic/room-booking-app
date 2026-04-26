package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService reservationService) {
        this.service = reservationService;
    }

    @PostMapping("/make")
    public ReservationResponse createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        return service.createReservation(request, username);
    }

    @GetMapping()
    public List<ReservationResponse> getAllReservations() {
        return service.getAllReservations();
    }

}
