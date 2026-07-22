package com.david.RoomReservation.service;

import com.david.RoomReservation.dto.user.PasswordRequest;
import com.david.RoomReservation.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    void deleteUser(Long id);

    void changePassword(String email, PasswordRequest request);

}
