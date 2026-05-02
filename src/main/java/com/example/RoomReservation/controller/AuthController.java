package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.email.ResendVerificationRequest;
import com.example.RoomReservation.dto.user.LoginRequest;
import com.example.RoomReservation.dto.user.LoginResponse;
import com.example.RoomReservation.dto.user.RegisterRequest;
import com.example.RoomReservation.dto.user.RegisterResponse;
import com.example.RoomReservation.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        service.verifyEmail(token);
        return ResponseEntity.ok("Verified");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(
            @RequestBody ResendVerificationRequest request) {

        service.resendVerificationEmail(request.email());
        return ResponseEntity.ok("Verification email sent");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

}
