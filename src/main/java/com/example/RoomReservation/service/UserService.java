package com.example.RoomReservation.service;

import com.example.RoomReservation.model.User;
import com.example.RoomReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<User> getUsers() {
        return repo.findAll();
    }

    public void deleteUser(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        repo.delete(user);
    }
}
