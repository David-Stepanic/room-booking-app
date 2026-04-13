package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.model.User;
import com.example.RoomReservation.repository.UserRepository;
import com.example.RoomReservation.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        repository.delete(user);
    }

}
