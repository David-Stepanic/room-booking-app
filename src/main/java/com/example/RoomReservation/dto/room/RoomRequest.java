package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {
    private Integer roomNumber;
    private Integer capacity;
    private RoomType roomType;
}