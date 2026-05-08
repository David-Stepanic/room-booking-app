package com.david.RoomReservation.mapper;

import com.david.RoomReservation.dto.user.LoginResponse;
import com.david.RoomReservation.dto.user.RegisterResponse;
import com.david.RoomReservation.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterResponse toRegisterResponse(User user);
    LoginResponse toLoginResponse(User user);
}
