package com.david.RoomReservation.repository;

import com.david.RoomReservation.model.User;
import com.david.RoomReservation.model.auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);
}
