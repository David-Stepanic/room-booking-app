package com.david.RoomReservation.service.impl;

import com.david.RoomReservation.dto.user.PasswordRequest;
import com.david.RoomReservation.exception.custom.ChangePasswordException;
import com.david.RoomReservation.mapper.UserMapper;
import com.david.RoomReservation.model.User;
import com.david.RoomReservation.repository.UserRepository;
import com.david.RoomReservation.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
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
