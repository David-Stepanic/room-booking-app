package com.example.RoomReservation.mapper;

import com.example.RoomReservation.dto.room.RoomRequest;
import com.example.RoomReservation.dto.room.RoomResponse;
import com.example.RoomReservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Room toEntity(RoomRequest request);
    RoomResponse toResponse(Room room);
}
