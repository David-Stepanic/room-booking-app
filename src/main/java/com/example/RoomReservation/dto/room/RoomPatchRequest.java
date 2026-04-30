package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RoomPatchRequest {
    @Min(1)
    private Integer roomNumber;
    @Min(1)
    private Integer capacity;
    private RoomType roomType;
}
