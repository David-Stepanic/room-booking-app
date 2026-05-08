package com.david.RoomReservation.dto.room;

import com.david.RoomReservation.model.constans.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoomRequest {

    @NotNull(message = "Room number is required!")
    private Integer roomNumber;

    @NotNull(message = "Capacity is required!")
    @Min(1)
    private Integer capacity;

    @NotNull(message = "Room type is required!")
    private RoomType roomType;
}