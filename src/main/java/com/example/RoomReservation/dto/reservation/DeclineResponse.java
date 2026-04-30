package com.example.RoomReservation.dto.reservation;

import com.example.RoomReservation.model.constans.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclineResponse {
    private ReservationStatus status;
    private String reason;

    public DeclineResponse(ReservationStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
