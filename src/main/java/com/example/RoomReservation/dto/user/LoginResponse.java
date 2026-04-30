package com.example.RoomReservation.dto.user;

import com.example.RoomReservation.model.constans.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String email;
    private String role;
}
