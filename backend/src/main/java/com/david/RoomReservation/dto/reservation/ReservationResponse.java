package com.david.RoomReservation.dto.reservation;

import com.david.RoomReservation.model.constans.ReservationStatus;
import com.david.RoomReservation.model.constans.RoomType;
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
    private String email;
    private String purpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private String declinedReason;
}
