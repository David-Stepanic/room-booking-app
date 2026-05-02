package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.user.PasswordRequest;
import com.example.RoomReservation.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    void deleteUser(Long id);

    void changePassword(String email, PasswordRequest request);
}
