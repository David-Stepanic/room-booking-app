package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.exception.custom.InvalidDateRangeException;
import com.example.RoomReservation.exception.custom.ReservationExistsException;
import com.example.RoomReservation.exception.custom.RoomNotFoundException;
import com.example.RoomReservation.mapper.ReservationMapper;
import com.example.RoomReservation.model.Reservation;
import com.example.RoomReservation.model.Room;
import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.constans.RoomType;
import com.example.RoomReservation.repository.ReservationRepository;
import com.example.RoomReservation.repository.RoomRepository;
import com.example.RoomReservation.repository.UserRepository;
import com.example.RoomReservation.service.ReservationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper mapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, ReservationMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest request, String username) {

        if (request.getStartTime().isAfter(request.getEndTime()))
            throw new InvalidDateRangeException("Start time must be before end time!");

        if (request.getStartTime().isBefore(LocalDateTime.now()))
            throw new InvalidDateRangeException("Start time cannot be in past!");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found!"));

        boolean isTaken = reservationRepository.reservationExists(
                room.getId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if(isTaken) {
            throw new ReservationExistsException("Room is already reserved in this time range");
        }

        Reservation reservation = new Reservation();
        reservation.setTitle(request.getTitle());
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setPurpose(resolvePurpose(room.getRoomType()));
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        reservationRepository.save(reservation);

        return mapper.toResponse(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private String resolvePurpose(RoomType type) {
        return switch (type) {
            case COMPUTER_ROOM -> "Computer classroom equipped for hands-on work and programming exercises";
            case TEACHING_ROOM -> "Lecture room intended for theoretical teaching and presentations";
            case COMPUTER_CENTER -> "Laboratory space designed for practical work, experiments, and group activities";
            case AMPHITHEATER -> "Large presentation hall suitable for lectures, conferences, and public speaking events";
        };
    }
}
