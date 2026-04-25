package com.example.RoomReservation.service.auth;

import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.constans.Role;
import com.example.RoomReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    private UserRepository repo;
    @Autowired
    private JWTService jwtService;

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return repo.save(user);
    }

    public String verify(User user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());

        return "Fail";
    }

}
