package com.example.RoomReservation.dto.email;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {}
