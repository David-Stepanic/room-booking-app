package com.david.RoomReservation.controller;

import com.david.RoomReservation.dto.user.PasswordRequest;
import com.david.RoomReservation.dto.user.RegisterResponse;
import com.david.RoomReservation.dto.user.UserPatchRequest;
import com.david.RoomReservation.model.User;
import com.david.RoomReservation.model.auth.UserPrincipal;
import com.david.RoomReservation.service.UserService;
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
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody PasswordRequest request) {

        service.changePassword(user.user().getEmail(), request);
        return ResponseEntity.ok("Password changed successfully!");
    }

    @PatchMapping("/edit-profile")
    public ResponseEntity<RegisterResponse> editUserProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody UserPatchRequest request) {
        return ResponseEntity.ok(service.editUserProfile(user.user().getEmail(), request));
    }


}
