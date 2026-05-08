package com.david.RoomReservation.mapper;

import com.david.RoomReservation.dto.room.RoomRequest;
import com.david.RoomReservation.dto.room.RoomResponse;
import com.david.RoomReservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Room toEntity(RoomRequest request);
    RoomResponse toResponse(Room room);
}
