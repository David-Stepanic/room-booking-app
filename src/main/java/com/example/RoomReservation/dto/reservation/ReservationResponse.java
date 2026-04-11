package com.example.RoomReservation.dto.reservation;

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
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
