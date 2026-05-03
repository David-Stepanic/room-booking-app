package com.example.RoomReservation.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordRequest {
    @NotBlank(message = "Old password is required!")
    private String oldPassword;
    @NotBlank(message = "New password is required!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)\\S{8,}$",
            message = "Password must have at least 8 characters, one uppercase, one lowercase and one special character without whitespaces."
    )
    private String newPassword;

}
