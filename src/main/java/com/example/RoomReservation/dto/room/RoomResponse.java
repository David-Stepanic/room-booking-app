package com.example.RoomReservation.dto.room;

import com.example.RoomReservation.model.constans.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private Integer roomNumber;
    private Integer capacity;
    private RoomType roomType;
}
