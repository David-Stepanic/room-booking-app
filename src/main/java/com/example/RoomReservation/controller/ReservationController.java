package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.reservation.DeclineRequest;
import com.example.RoomReservation.dto.reservation.DeclineResponse;
import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.model.constans.ReservationStatus;
import com.example.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

        String email = authentication.getName();

        return service.createReservation(request, email);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully");
    }

    @GetMapping()
    public List<ReservationResponse> getAllReservations() {
        return service.getAllReservations();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/confirm/{id}")
    public ResponseEntity<ReservationStatus> confirmReservation(@PathVariable Long id) {
        ReservationResponse response = service.confirmReservation(id);

        return ResponseEntity.ok(response.getReservationStatus());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/cancel/{id}")
    public ResponseEntity<ReservationStatus> cancelReservation(@PathVariable Long id) {
        ReservationResponse response = service.cancelReservation(id);
        return ResponseEntity.ok(response.getReservationStatus());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/decline/{id}")
    public ResponseEntity<DeclineResponse> declineReservation(
            @PathVariable Long id,
            @RequestBody DeclineRequest request) {

        ReservationResponse response = service.declineReservation(id, request.getReason());

        DeclineResponse d = new DeclineResponse(
                response.getReservationStatus(),
                response.getDeclinedReason()
        );

        return ResponseEntity.ok(d);
    }

}
