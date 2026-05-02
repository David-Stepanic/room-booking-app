package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.user.PasswordRequest;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.auth.UserPrincipal;
import com.example.RoomReservation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<User> getUsers() {
        return service.getUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody PasswordRequest request) {

        service.changePassword(user.user().getEmail(), request);
        return ResponseEntity.ok("Password changed successfully!");
    }



}
