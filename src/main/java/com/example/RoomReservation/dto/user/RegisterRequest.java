package com.example.RoomReservation.dto.user;

import com.example.RoomReservation.model.constans.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@(gmail\\.com|outlook\\.com)$",
            message = "Only gmail.com and outlook.com are allowed!"
    )
    private String email;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only letters are allowed!")
    @NotBlank(message = "First name is required!")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only letters are allowed!")
    @NotBlank(message = "Last name is required!")
    private String lastName;
    @NotBlank(message = "Password is required!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)\\S{8,}$",
            message = "Password must have at least 8 characters, one uppercase, one lowercase and one special character without whitespaces."
    )
    private String password;
    @Pattern(regexp = "^\\d{3,4}/(20\\d{2})$", message = "Valid index number format is 3821/2025!")
    private String indexNumber;
    private Department department;
}



