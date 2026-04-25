package com.example.RoomReservation.controller;

import com.example.RoomReservation.dto.room.RoomRequest;
import com.example.RoomReservation.dto.room.RoomResponse;
import com.example.RoomReservation.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping()
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        return roomService.getAvailableRooms(startTime, endTime);
    }


    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping()
    public RoomResponse createRoom(@RequestBody RoomRequest room) {
        return roomService.createRoom(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }


}
