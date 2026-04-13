package com.example.RoomReservation.service;

import com.example.RoomReservation.dto.room.RoomRequest;
import com.example.RoomReservation.dto.room.RoomResponse;
import com.example.RoomReservation.model.Room;
import java.util.List;

public interface RoomService {

    List<RoomResponse> getAllRooms();
    RoomResponse createRoom(RoomRequest room);
    RoomResponse getRoomById(Long id);
    void deleteRoom(Long id);

}
