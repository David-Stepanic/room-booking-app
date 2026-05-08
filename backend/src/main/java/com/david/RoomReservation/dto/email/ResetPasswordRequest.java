package com.david.RoomReservation.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        String token,
        @NotBlank(message = "Password is required!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)\\S{8,}$",
                message = "Password must have at least 8 characters, one uppercase, one lowercase and one special character without whitespaces."
        )
        String newPassword
) {}
