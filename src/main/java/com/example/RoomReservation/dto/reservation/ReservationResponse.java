package com.example.RoomReservation.dto.reservation;

import com.example.RoomReservation.model.constans.ReservationStatus;
import com.example.RoomReservation.model.constans.RoomType;
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
    private String name;
    private String purpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RoomType reservationType;
    private ReservationStatus reservationStatus;
    private String declinedReason;
}
