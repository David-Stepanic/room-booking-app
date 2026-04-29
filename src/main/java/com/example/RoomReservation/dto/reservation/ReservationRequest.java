package com.example.RoomReservation.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    @NotBlank(message = "Room is required")
    private Long roomId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Start time is required")
    private LocalDateTime startTime;

    @NotBlank(message = "End time is required")
    private LocalDateTime endTime;
}
