package com.david.RoomReservation.mapper;

import com.david.RoomReservation.dto.reservation.ReservationResponse;
import com.david.RoomReservation.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReservationMapper {
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "roomType", source = "room.roomType")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    ReservationResponse toResponse(Reservation reservation);
}
