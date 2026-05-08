package com.david.RoomReservation.dto.user;

import com.david.RoomReservation.model.constans.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Department department;
}
