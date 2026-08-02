package com.david.RoomReservation.reservation;

import com.david.RoomReservation.dto.reservation.DeclineRequest;
import com.david.RoomReservation.dto.reservation.ReservationRequest;
import com.david.RoomReservation.dto.reservation.ReservationResponse;
import com.david.RoomReservation.exception.custom.InvalidDateRangeException;
import com.david.RoomReservation.exception.custom.ReservationException;
import com.david.RoomReservation.exception.custom.ReservationNotFoundException;
import com.david.RoomReservation.exception.custom.RoomException;
import com.david.RoomReservation.mapper.ReservationMapper;
import com.david.RoomReservation.model.*;
import com.david.RoomReservation.model.constans.*;
import com.david.RoomReservation.repository.*;
import com.david.RoomReservation.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.david.RoomReservation.model.constans.Department.SOFTWARE_ENGINEERING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ReservationMapper mapper;

    private ReservationServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReservationServiceImpl(reservationRepository, userRepository, roomRepository, mapper);
    }

    @Test
    void shouldCreateReservation() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, ReservationStatus.PENDING, start,
                null, null, user, room);
        ReservationRequest req = new ReservationRequest(5L,"Task overview", start, end);

        given(userRepository.findByEmail("test@gmail.com"))
                .willReturn(Optional.of(user));

        given(roomRepository.findById(5L))
                .willReturn(Optional.of(room));

        given(reservationRepository.reservationExists(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(false);


        // when
        underTest.createReservation(req, user.getEmail());

        // then
        ArgumentCaptor<Reservation> reservationArgumentCaptor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository)
                .save(reservationArgumentCaptor.capture());

        Reservation capturedReservation = reservationArgumentCaptor.getValue();

        assertThat(capturedReservation.getTitle()).isEqualTo(req.getTitle());
        assertThat(capturedReservation.getUser()).isEqualTo(user);
        assertThat(capturedReservation.getRoom()).isEqualTo(room);
    }

    @Test
    void willThrowWhenStartTimeIsAfterEndTime() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(3);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        User user = new User("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING, true);
        Room room = new Room(5L, 101, 50, RoomType.COMPUTER_ROOM, null);
        ReservationRequest req = new ReservationRequest(5L, "Task overview", start, end);

        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(5L)).willReturn(Optional.of(room));
        given(reservationRepository.reservationExists(anyLong(), any(), any())).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.createReservation(req, "test@gmail.com"))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("Start time must be before end time!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenStartTimeIsInThePast() {
        // given
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        User user = new User("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING, true);
        Room room = new Room(5L, 101, 50, RoomType.COMPUTER_ROOM, null);
        ReservationRequest req = new ReservationRequest(5L, "Task overview", start, end);

        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(5L)).willReturn(Optional.of(room));
        given(reservationRepository.reservationExists(anyLong(), any(), any())).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.createReservation(req, "test@gmail.com"))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("Start time cannot be in the past!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenUserEmailIsNotFound() {
        // given
        String email = "testError@gmail.com";
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        ReservationRequest req = new ReservationRequest(5L,"Task overview", start, end);

        given(userRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.createReservation(req, "test@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Email not found!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenRoomIsNotFound() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        ReservationRequest req = new ReservationRequest(3L,"Task overview", start, end);

        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(user));

        given(roomRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then

        assertThatThrownBy(() -> underTest.createReservation(req, "test@gmail.com"))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Room not found!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenRoomIsAlreadyReserved() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);

        ReservationRequest req = new ReservationRequest(3L,"Task overview", start, end);

        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(user));

        given(roomRepository.findById(any()))
                .willReturn(Optional.of(room));

        given(reservationRepository.reservationExists(any(), any(), any()))
                .willReturn(true);

        // when
        // then

        assertThatThrownBy(() -> underTest.createReservation(req, "test@gmail.com"))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Room is already reserved in this time range");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllReservations() {
        // given
        Reservation reservation = new Reservation();
        given(reservationRepository.findAll())
                .willReturn(List.of(reservation));
        given(mapper.toResponse(any()))
                .willReturn(mock(ReservationResponse.class));

        // when
        List<ReservationResponse> result = underTest.getAllReservations();

        //then
        assertThat(result).hasSize(1);
        verify(reservationRepository).findAll();
        verify(mapper).toResponse(reservation);
    }

    @Test
    void shouldConfirmReservation() {
        // given
        Long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setReservationStatus(ReservationStatus.PENDING);

        ReservationResponse response = mock(ReservationResponse.class);

        given(reservationRepository.findById(id))
                .willReturn(Optional.of(reservation));

        given(mapper.toResponse(reservation))
                .willReturn(response);

        // when
        ReservationResponse result = underTest.confirmReservation(id);

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
        assertThat(result).isEqualTo(response);

        verify(reservationRepository).save(any());
    }

    @Test
    void willThrowWhenReservationNotFoundOnConfirm() {
        // given
        given(reservationRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.confirmReservation(1L))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Reservation does not exist!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenConfirmingNonPendingReservation() {
        // given
        Long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        given(reservationRepository.findById(id))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.confirmReservation(id))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be confirmed!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldCancelReservation() {
        // given
        Long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setReservationStatus(ReservationStatus.PENDING);

        ReservationResponse response = mock(ReservationResponse.class);

        given(reservationRepository.findById(id))
                .willReturn(Optional.of(reservation));

        given(mapper.toResponse(reservation))
                .willReturn(response);

        // when
        ReservationResponse result = underTest.cancelReservation(id);

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CANCELED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
        assertThat(result).isEqualTo(response);

        verify(reservationRepository).save(any());
    }

    @Test
    void willThrowWhenReservationNotFoundOnCancel() {
        // given
        Long id = 1L;
        given(reservationRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.cancelReservation(id))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Reservation does not exist!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenCancelingNonPendingReservation() {
        // given
        Long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        given(reservationRepository.findById(id))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.cancelReservation(id))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be canceled!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldCancelOwnReservation() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, ReservationStatus.PENDING, start,
                null, null, user, room);

        ReservationResponse response = mock(ReservationResponse.class);

        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));
        given(mapper.toResponse(reservation))
                .willReturn(response);

        // when
        ReservationResponse result = underTest.cancelOwnReservation(reservation.getId(), "test@gmail.com");

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CANCELED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
        assertThat(result).isEqualTo(response);

        verify(reservationRepository).save(any());
    }

    @Test
    void willThrowWhenReservationNotFoundOnCancelOwn() {
        // given
        Long id = 1L;
        given(reservationRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.cancelOwnReservation(id,"test@gmail.com"))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Reservation does not exist!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenReservationIfItIsNotOwn() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, ReservationStatus.PENDING, start,
                null, null, user, room);

        given(reservationRepository.findById(any()))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.cancelOwnReservation(1L, "test2@gmail.com"))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("You can only cancel your own reservations!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenCancelingOwnNonPendingReservation() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, null, start,
                null, null, user, room);

        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.cancelOwnReservation(reservation.getId(), "test@gmail.com"))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be canceled!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldDeclineReservation() {
        // given
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setId(1L);
        DeclineRequest req = new DeclineRequest("Unavailable");

        ReservationResponse response = mock(ReservationResponse.class);

        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));

        given(mapper.toResponse(reservation))
                .willReturn(response);

        // when
        ReservationResponse result = underTest.declineReservation(1L, req);

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.DECLINED);
        assertThat(reservation.getDeclinedReason()).isNotNull();
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
        assertThat(result).isEqualTo(response);

        verify(reservationRepository).save(any());
    }

    @Test
    void willThrowWhenReservationNotFoundOnDecline() {
        // given
        Long id = 1L;
        given(reservationRepository.findById(id))
                .willReturn(Optional.empty());
        DeclineRequest req = new DeclineRequest("Unavailable");

        // when
        // then
        assertThatThrownBy(() -> underTest.declineReservation(id, req))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Reservation does not exist!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenDecliningNonPendingReservation() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, null, start,
                null, null, user, room);
        DeclineRequest req = new DeclineRequest("Unavailable");

        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.declineReservation(reservation.getId(), req))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be declined!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void willThrowWhenDeclinedReasonIsNullOrBlank() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, ReservationStatus.PENDING, start,
                null, null, user, room);
        DeclineRequest req = new DeclineRequest(null);

        given(reservationRepository.findById(1L))
                .willReturn(Optional.of(reservation));

        // when
        // then
        assertThatThrownBy(() -> underTest.declineReservation(1L, req))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Decline reason is required!");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldDeleteReservation() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        Room room = new Room (5L,101, 50, RoomType.COMPUTER_ROOM, null);
        Reservation reservation = new Reservation (
                1L,
                "Task overview",
                "Analyzing solution",
                start, end, ReservationStatus.PENDING, start,
                null, null, user, room);

        given(reservationRepository.findById(1L))
                .willReturn(Optional.of(reservation));

        // when
        underTest.deleteReservation(reservation.getId());

        // then
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void willThrowWhenReservationNotFoundOnDelete() {
        // given
        Long id = 1L;
        given(reservationRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteReservation(id))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("Reservation does not exist!");

        verify(reservationRepository, never()).delete(any());
    }
}
