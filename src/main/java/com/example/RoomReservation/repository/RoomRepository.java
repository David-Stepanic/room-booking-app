package com.example.RoomReservation.repository;

import com.example.RoomReservation.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
                SELECT r FROM Room r
                WHERE NOT EXISTS (
                    SELECT res FROM Reservation res
                    WHERE res.room = r
                    AND res.startTime < :end
                    AND res.endTime > :start
                )
            """)
    List<Room> findAvailableRooms(
            @Param("start") LocalDateTime startTime,
            @Param("end") LocalDateTime endTime);

}
