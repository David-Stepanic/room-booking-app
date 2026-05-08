package com.david.RoomReservation.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String email;
    private String role;
}
