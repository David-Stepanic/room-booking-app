package com.example.RoomReservation.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private Long id;
    private String username;
    private String role;
}
