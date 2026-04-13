package com.example.RoomReservation.dto.reservation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {

    private Long roomId;
    private boolean available;

}
