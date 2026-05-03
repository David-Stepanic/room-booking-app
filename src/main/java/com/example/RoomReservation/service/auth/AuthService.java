package com.example.RoomReservation.service.auth;

import com.example.RoomReservation.dto.email.ForgotPasswordRequest;
import com.example.RoomReservation.dto.email.ResetPasswordRequest;
import com.example.RoomReservation.dto.user.LoginRequest;
import com.example.RoomReservation.dto.user.LoginResponse;
import com.example.RoomReservation.dto.user.RegisterRequest;
import com.example.RoomReservation.dto.user.RegisterResponse;
import com.example.RoomReservation.exception.custom.TokenException;
import com.example.RoomReservation.exception.custom.UserExistsException;
import com.example.RoomReservation.exception.custom.VerifyMailException;
import com.example.RoomReservation.mapper.UserMapper;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.auth.PasswordResetToken;
import com.example.RoomReservation.model.auth.UserPrincipal;
import com.example.RoomReservation.model.auth.VerificationToken;
import com.example.RoomReservation.model.constans.Role;
import com.example.RoomReservation.repository.PasswordResetTokenRepository;
import com.example.RoomReservation.repository.UserRepository;
import com.example.RoomReservation.repository.VerificationTokenRepository;
import com.example.RoomReservation.service.email.EmailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    private UserRepository repo;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private PasswordResetTokenRepository resetRepo;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserMapper mapper;

    public RegisterResponse register(RegisterRequest request) {

        if (repo.existsByEmail(request.getEmail())) {
            throw new UserExistsException("Email already exists");
        }

        if (repo.existsByIndexNumber(request.getIndexNumber()) && request.getIndexNumber() != null) {
            throw new UserExistsException("Index number already exists");
        }

        User user = new User();
        user.setEnabled(false);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        if (request.getDepartment() != null)
            user.setDepartment(request.getDepartment());
        if (request.getIndexNumber() != null)
            user.setIndexNumber(request.getIndexNumber());

        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user, LocalDateTime.now().plusHours(1));

        repo.save(user);
        tokenRepository.save(token);
        emailService.sendVerificationEmail(token);

        return mapper.toRegisterResponse(user);
    }

    @Transactional
    public void verifyEmail(String tokenValue) {

        VerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new VerifyMailException("Invalid or expired verification link"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VerifyMailException("Token expired");
        }

        User user = token.getUser();

        if (user.isEnabled()) {
            throw new VerifyMailException("Account already verified");
        }

        user.setEnabled(true);

        repo.save(user);
        tokenRepository.deleteById(token.getId());
    }

    public LoginResponse login(LoginRequest request) {

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

        if (!user.isEnabled()) {
            throw new VerifyMailException("Verify your email first");
        }
        LoginResponse response = mapper.toLoginResponse(user);
        response.setToken(jwtService.generateToken(user.getEmail()));

        return response;
    }

    @Transactional
    public void resendVerificationEmail(String email) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new VerifyMailException("User not found"));

        if (user.isEnabled()) {
            throw new VerifyMailException("User already verified");
        }

        tokenRepository.deleteByUser(user);

        VerificationToken token = new VerificationToken(
                UUID.randomUUID().toString(),
                user,
                LocalDateTime.now().plusHours(1)
        );

        tokenRepository.save(token);

        emailService.sendVerificationEmail(token);
    }

    public void forgotPassword(ForgotPasswordRequest request) {

        User user = repo.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        resetRepo.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken(
                token,
                user,
                LocalDateTime.now().plusMinutes(30)
        );

        resetRepo.save(resetToken);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = resetRepo.findByToken(request.token())
                .orElseThrow(() -> new TokenException("Invalid token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenException("Token expired");
        }

        User user = token.getUser();

        user.setPassword(encoder.encode(request.newPassword()));

        repo.save(user);

        resetRepo.delete(token);
    }

}
