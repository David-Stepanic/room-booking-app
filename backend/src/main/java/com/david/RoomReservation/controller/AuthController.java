package com.david.RoomReservation.controller;

import com.david.RoomReservation.dto.email.ForgotPasswordRequest;
import com.david.RoomReservation.dto.email.ResendVerificationRequest;
import com.david.RoomReservation.dto.email.ResetPasswordRequest;
import com.david.RoomReservation.dto.user.LoginRequest;
import com.david.RoomReservation.dto.user.LoginResponse;
import com.david.RoomReservation.dto.user.RegisterRequest;
import com.david.RoomReservation.dto.user.RegisterResponse;
import com.david.RoomReservation.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        service.verifyEmail(token);
        return ResponseEntity.ok("Email is verified!");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(
            @RequestBody ResendVerificationRequest request) {

        service.resendVerificationEmail(request.email());
        return ResponseEntity.ok("Verification email sent!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        service.forgotPassword(request);
        return ResponseEntity.ok("Reset link sent to email!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully!");
    }

}
