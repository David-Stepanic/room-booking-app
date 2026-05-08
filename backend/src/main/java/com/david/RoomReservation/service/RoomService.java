package com.david.RoomReservation.service;

import com.david.RoomReservation.dto.room.RoomPatchRequest;
import com.david.RoomReservation.dto.room.RoomRequest;
import com.david.RoomReservation.dto.room.RoomResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime);

    RoomResponse createRoom(RoomRequest room);

    RoomResponse getRoomById(Long id);

    void deleteRoom(Long id);

    RoomResponse editRoom(RoomPatchRequest req, Long id);
}
