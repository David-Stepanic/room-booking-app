package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.model.Reservation;
import com.example.RoomReservation.model.Room;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.repository.ReservationRepository;
import com.example.RoomReservation.repository.RoomRepository;
import com.example.RoomReservation.repository.UserRepository;
import com.example.RoomReservation.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository resrvationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public ReservationServiceImpl(ReservationRepository resrvationRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.resrvationRepository = resrvationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {

        Optional<User> user = userRepository.findById(request.getUserId());
        Optional<Room> room = roomRepository.findById(request.getRoomId());




        return null;
    }
}
