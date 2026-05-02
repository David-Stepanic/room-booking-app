package com.example.RoomReservation.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordRequest {
    @NotBlank(message = "Old password is required!")
    private String oldPassword;
    @NotBlank(message = "New password is required!")
    private String newPassword;

}
