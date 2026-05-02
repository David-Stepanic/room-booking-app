package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.dto.user.PasswordRequest;
import com.example.RoomReservation.exception.custom.ChangePasswordException;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.repository.UserRepository;
import com.example.RoomReservation.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        repository.delete(user);
    }

    @Transactional
    @Override
    public void changePassword(String email, PasswordRequest request) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ChangePasswordException("Old password is incorrect");
        }

        if (encoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ChangePasswordException("New password must be different");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
    }

}
