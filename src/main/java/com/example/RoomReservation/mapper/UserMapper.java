package com.example.RoomReservation.mapper;

import com.example.RoomReservation.dto.user.LoginResponse;
import com.example.RoomReservation.dto.user.RegisterResponse;
import com.example.RoomReservation.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterResponse toRegisterResponse(User user);
    LoginResponse toLoginResponse(User user);
}
