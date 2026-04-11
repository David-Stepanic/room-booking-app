package com.example.RoomReservation.service;

import com.example.RoomReservation.model.Room;
import com.example.RoomReservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public List<Room> getRooms() {
        return repo.findAll();
    }

    public Room createRoom(Room room) {
        return repo.save(room);
    }

    public Room getRoom(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room does not exist!"));
    }

    public void deleteRoom(Long id) {
        Room room = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        repo.delete(room);
    }


}
