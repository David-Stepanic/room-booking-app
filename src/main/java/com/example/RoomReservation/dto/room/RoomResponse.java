package com.example.RoomReservation.dto.room;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private Integer capacity;
    private String location;
    private String description;
}