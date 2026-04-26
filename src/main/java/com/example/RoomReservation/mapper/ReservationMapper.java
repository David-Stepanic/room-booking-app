package com.example.RoomReservation.mapper;

import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReservationMapper {
    @Mapping(target = "username", source = "user.username")
    ReservationResponse toResponse(Reservation reservation);
}
