package com.example.RoomReservation.controller;

import com.example.RoomReservation.model.Room;
import com.example.RoomReservation.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping()
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    @PostMapping()
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }


}
