package com.example.RoomReservation.service;

import com.example.RoomReservation.model.User;
import java.util.List;

public interface UserService {

    List<User> getUsers();
    void deleteUser(Long id);

}
