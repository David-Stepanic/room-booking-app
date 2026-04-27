package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import lombok.Getter;

@Getter
public class RoomPatchRequest {
    private Integer roomNumber;
    private Integer capacity;
    private RoomType roomType;
}
