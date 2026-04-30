package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
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
    public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
        service.confirmReservation(id);
        return ResponseEntity.ok("Reservation confirmed.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/cancel/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        service.cancelReservation(id);
        return ResponseEntity.ok("Reservation canceled.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/decline/{id}")
    public ResponseEntity<?> declineReservation(@PathVariable Long id) {
        service.declineReservation(id);
        return ResponseEntity.ok("Reservation declined.");
    }

}
