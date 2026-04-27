package com.example.RoomReservation.dto.reservation;

import com.example.RoomReservation.model.constans.ReservationStatus;
import com.example.RoomReservation.model.constans.RoomType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private String username;
    private String purpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private String declinedReason;
}
