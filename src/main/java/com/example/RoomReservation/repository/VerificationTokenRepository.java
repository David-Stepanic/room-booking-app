package com.example.RoomReservation.repository;

import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);
}
