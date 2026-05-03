package com.example.RoomReservation.dto.user;

import com.example.RoomReservation.model.constans.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPatchRequest {
    @Email(message = "Invalid email format!")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@(gmail\\.com|outlook\\.com)$",
            message = "Only gmail.com and outlook.com are allowed!"
    )
    private String email;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only letters are allowed!")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only letters are allowed!")
    private String lastName;
    private Department department;
}
