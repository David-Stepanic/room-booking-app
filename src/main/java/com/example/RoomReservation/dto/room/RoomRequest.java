package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RoomRequest {

    @NotBlank(message = "Room number is required!")
    private Integer roomNumber;

    @NotBlank(message = "Capacity is required!")
    @Min(1)
    private Integer capacity;

    @NotBlank(message = "Room type is required!")
    private RoomType roomType;
}