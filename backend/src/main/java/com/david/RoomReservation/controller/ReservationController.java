package com.david.RoomReservation.controller;

import com.david.RoomReservation.dto.reservation.DeclineRequest;
import com.david.RoomReservation.dto.reservation.ReservationRequest;
import com.david.RoomReservation.dto.reservation.ReservationResponse;
import com.david.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(request, email));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public List<ReservationResponse> getAllReservations() {
        return service.getAllReservations();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/confirm/{id}")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Long id) {
        ReservationResponse response = service.confirmReservation(id);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/cancel/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        ReservationResponse response = service.cancelReservation(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/decline/{id}")
    public ResponseEntity<ReservationResponse> declineReservation(
            @PathVariable Long id,
            @RequestBody DeclineRequest request) {

        return ResponseEntity.ok(service.declineReservation(id, request));
    }

}
