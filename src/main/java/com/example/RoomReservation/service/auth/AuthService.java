package com.example.RoomReservation.service.auth;

import com.example.RoomReservation.dto.user.LoginRequest;
import com.example.RoomReservation.dto.user.LoginResponse;
import com.example.RoomReservation.dto.user.RegisterRequest;
import com.example.RoomReservation.dto.user.RegisterResponse;
import com.example.RoomReservation.exception.custom.UserExistsException;
import com.example.RoomReservation.mapper.UserMapper;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.UserPrincipal;
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
    @Autowired
    private UserMapper mapper;

    public RegisterResponse register(RegisterRequest request) {

        if (repo.existsByEmail(request.getEmail())) {
            throw new UserExistsException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User saved = repo.save(user);

        return mapper.toRegisterResponse(saved);
    }

    public LoginResponse verify(LoginRequest request) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserPrincipal userDetails =
                (UserPrincipal) authentication.getPrincipal();

        User user = userDetails.user();
        LoginResponse response = mapper.toLoginResponse(user);
        response.setToken(jwtService.generateToken(user.getEmail()));

        return response;
    }

}
