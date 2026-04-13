package com.example.RoomReservation.dto.reservation;

import com.example.RoomReservation.model.constans.*;
import lombok.*;
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
