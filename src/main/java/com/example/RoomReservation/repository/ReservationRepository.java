package com.example.RoomReservation.repository;

import com.example.RoomReservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1
            FROM Reservation r
            WHERE r.room.id = :roomId
            AND r.startTime < :endTime
            AND r.endTime > :startTime
        ) THEN true ELSE false END
    """)
    boolean reservationExists(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
