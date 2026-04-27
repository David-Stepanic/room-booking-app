package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.room.RoomRequest;
import com.example.RoomReservation.dto.room.RoomResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime);

    RoomResponse createRoom(RoomRequest room);

    RoomResponse getRoomById(Long id);

    void deleteRoom(Long id);

    RoomResponse editRoom(RoomRequest req, Long id);
}
