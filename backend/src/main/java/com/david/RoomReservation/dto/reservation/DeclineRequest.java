package com.david.RoomReservation.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeclineRequest {

    @NotBlank(message = "Decline reason is required!")
    private String reason;

    public DeclineRequest() {
    }

    public DeclineRequest(String reason) {
        this.reason = reason;
    }
}
