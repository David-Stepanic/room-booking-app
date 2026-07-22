package com.david.RoomReservation.service.impl;

import com.david.RoomReservation.dto.reservation.DeclineRequest;
import com.david.RoomReservation.dto.reservation.ReservationRequest;
import com.david.RoomReservation.dto.reservation.ReservationResponse;
import com.david.RoomReservation.exception.custom.ReservationException;
import com.david.RoomReservation.exception.custom.ReservationNotFoundException;
import com.david.RoomReservation.exception.custom.RoomException;
import com.david.RoomReservation.mapper.ReservationMapper;
import com.david.RoomReservation.model.Reservation;
import com.david.RoomReservation.model.Room;
import com.david.RoomReservation.model.User;
import com.david.RoomReservation.model.constans.RoomType;
import com.david.RoomReservation.repository.ReservationRepository;
import com.david.RoomReservation.repository.RoomRepository;
import com.david.RoomReservation.repository.UserRepository;
import com.david.RoomReservation.service.ReservationService;
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
    public ReservationResponse cancelOwnReservation(Long id, String email) {
        Reservation reservation = getReservationOrThrow(id);

        if (!reservation.getUser().getEmail().equals(email)) {
            throw new ReservationException("You can only cancel your own reservations!");
        }

        reservation.cancel();

        reservationRepository.save(reservation);

        return mapper.toResponse(reservation);
    }

    @Override
    public ReservationResponse declineReservation(Long id, DeclineRequest request) {
        Reservation reservation = getReservationOrThrow(id);

        reservation.decline(request.getReason());

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
