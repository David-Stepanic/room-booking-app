package com.example.RoomReservation.dto.user;

import com.example.RoomReservation.model.constans.Department;
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
    private String indexNumber;
    private Department department;
}
