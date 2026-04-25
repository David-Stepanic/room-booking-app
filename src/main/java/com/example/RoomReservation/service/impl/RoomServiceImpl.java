package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.dto.room.RoomRequest;
import com.example.RoomReservation.dto.room.RoomResponse;
import com.example.RoomReservation.exception.InvalidDateRangeException;
import com.example.RoomReservation.mapper.RoomMapper;
import com.example.RoomReservation.model.Room;
import com.example.RoomReservation.repository.RoomRepository;
import com.example.RoomReservation.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    public RoomServiceImpl(RoomRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RoomResponse> getAllRooms() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(endTime))
            throw new InvalidDateRangeException("Invalid range, startTime must be before endTime!");

        return repository.findAvailableRooms(startTime, endTime)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public RoomResponse createRoom(RoomRequest request) {
        Room room = mapper.toEntity(request);
        Room dbRoom = repository.save(room);

        return mapper.toResponse(dbRoom);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room does not exist!"));
        return mapper.toResponse(room);
    }

    public void deleteRoom(Long id) {
        Room room = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        repository.delete(room);
    }

}
