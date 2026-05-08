package com.david.RoomReservation.init;

import com.david.RoomReservation.model.User;
import com.david.RoomReservation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeedPasswordEncoder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedPasswordEncoder(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        List<User> users = userRepository.findAll();

        for (User user : users) {

            String password = user.getPassword();

            if (password != null && password.length() < 60) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
        }
    }
}
