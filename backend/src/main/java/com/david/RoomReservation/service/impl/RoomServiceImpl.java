package com.david.RoomReservation.service.impl;

import com.david.RoomReservation.dto.room.RoomPatchRequest;
import com.david.RoomReservation.dto.room.RoomRequest;
import com.david.RoomReservation.dto.room.RoomResponse;
import com.david.RoomReservation.exception.custom.InvalidDateRangeException;
import com.david.RoomReservation.exception.custom.RoomNotFoundException;
import com.david.RoomReservation.mapper.RoomMapper;
import com.david.RoomReservation.model.Room;
import com.david.RoomReservation.repository.RoomRepository;
import com.david.RoomReservation.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper mapper;

    public RoomServiceImpl(RoomRepository repository, RoomMapper mapper) {
        this.roomRepository = repository;
        this.mapper = mapper;
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(endTime))
            throw new InvalidDateRangeException("Invalid range, startTime must be before endTime!");

        return roomRepository.findAvailableRooms(startTime, endTime)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public RoomResponse createRoom(RoomRequest request) {
        Room room = mapper.toEntity(request);

        room.updateCapacity(request.getCapacity());
        room.updateRoomNumber(request.getRoomNumber());

        roomRepository.save(room);
        return mapper.toResponse(room);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room does not exist!"));
        return mapper.toResponse(room);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found!"));
        roomRepository.delete(room);
    }

    @Override
    public RoomResponse editRoom(RoomPatchRequest req, Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found!"));
        if (req.getRoomType() != null)
            room.setRoomType(req.getRoomType());
        if (req.getRoomNumber() != null)
            room.updateRoomNumber(req.getRoomNumber());
        if (req.getCapacity() != null) {
            room.updateCapacity(req.getCapacity());
        }
        roomRepository.save(room);

        return mapper.toResponse(room);
    }

}
