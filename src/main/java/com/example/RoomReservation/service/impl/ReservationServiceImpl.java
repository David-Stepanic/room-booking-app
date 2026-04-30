package com.example.RoomReservation.service.impl;

import com.example.RoomReservation.dto.reservation.ReservationRequest;
import com.example.RoomReservation.dto.reservation.ReservationResponse;
import com.example.RoomReservation.exception.custom.ReservationException;
import com.example.RoomReservation.exception.custom.ReservationNotFoundException;
import com.example.RoomReservation.exception.custom.RoomException;
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
    public ReservationResponse createReservation(ReservationRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomException("Room not found!"));

        boolean isTaken = reservationRepository.reservationExists(
                room.getId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if(isTaken) {
            throw new ReservationException("Room is already reserved in this time range");
        }

        Reservation reservation = new Reservation();
        reservation.setTitle(request.getTitle());
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setPurpose(resolvePurpose(room.getRoomType()));
        reservation.schedule(request.getStartTime(), request.getEndTime());

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

    @Override
    public ReservationResponse confirmReservation(Long id) {
        Reservation reservation = getReservationOrThrow(id);

        reservation.confirm();

        reservationRepository.save(reservation);

        return mapper.toResponse(reservation);
    }

    @Override
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = getReservationOrThrow(id);

        reservation.cancel();

        reservationRepository.save(reservation);

        return mapper.toResponse(reservation);
    }

    @Override
    public ReservationResponse declineReservation(Long id, String reason) {
        Reservation reservation = getReservationOrThrow(id);

        reservation.decline(reason);

        reservationRepository.save(reservation);

        return mapper.toResponse(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = getReservationOrThrow(id);

        reservationRepository.delete(reservation);
    }

    private Reservation getReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation does not exist!"));
    }
}
