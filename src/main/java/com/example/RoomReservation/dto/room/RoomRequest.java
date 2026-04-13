package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import lombok.*;

@Getter
@Setter
public class RoomRequest {
    private int roomNumber;
    private int capacity;
    private RoomType roomType;
}