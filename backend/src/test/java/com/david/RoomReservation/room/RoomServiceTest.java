package com.david.RoomReservation.room;

import com.david.RoomReservation.dto.room.RoomPatchRequest;
import com.david.RoomReservation.dto.room.RoomRequest;
import com.david.RoomReservation.dto.room.RoomResponse;
import com.david.RoomReservation.exception.custom.InvalidDateRangeException;
import com.david.RoomReservation.exception.custom.RoomException;
import com.david.RoomReservation.exception.custom.RoomNotFoundException;
import com.david.RoomReservation.mapper.RoomMapper;
import com.david.RoomReservation.model.Room;
import com.david.RoomReservation.model.constans.RoomType;
import com.david.RoomReservation.repository.RoomRepository;
import com.david.RoomReservation.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.david.RoomReservation.model.constans.RoomType.COMPUTER_ROOM;
import static com.david.RoomReservation.model.constans.RoomType.TEACHING_ROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomMapper mapper;

    private RoomServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoomServiceImpl(roomRepository, mapper);
    }

    @Test
    void shouldReturnAllRooms() {
        // given
        Room room = new Room();
        given(roomRepository.findAll())
                .willReturn(List.of(room));
        given(mapper.toResponse(room))
                .willReturn(mock(RoomResponse.class));

        // when
        List<RoomResponse> result = underTest.getAllRooms();

        // then
        assertThat(result).hasSize(1);
        verify(roomRepository).findAll();
        verify(mapper).toResponse(room);
    }

    @Test
    void shouldGetAllAvailableRooms() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        Room room = new Room();

        given(roomRepository.findAvailableRooms(start, end))
                .willReturn(List.of(room));
        given(mapper.toResponse(room))
                .willReturn(mock(RoomResponse.class));

        // when
        List<RoomResponse> result = underTest.getAvailableRooms(start, end);

        // then
        assertThat(result).hasSize(1);
        verify(roomRepository).findAvailableRooms(start, end);
    }

    @Test
    void willThrowWhenStartTimeIsAfterEndTimeOnGetAvailable() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(3);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        // when
        // then
        assertThatThrownBy(() -> underTest.getAvailableRooms(start, end))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("Invalid range, startTime must be before endTime!");

        verify(roomRepository, never()).findAvailableRooms(any(), any());
    }

    @Test
    void shouldCreateRoom() {
        // given
        Room room = new Room();
        RoomRequest req = new RoomRequest(30, 101, COMPUTER_ROOM);
        RoomResponse response = mock(RoomResponse.class);

        given(mapper.toEntity(req))
                .willReturn(room);
        given(mapper.toResponse(room))
                .willReturn(response);

        // when
        RoomResponse result = underTest.createRoom(req);

        // then
        assertThat(req.getRoomType()).isNotNull();
        assertThat(req.getCapacity()).isGreaterThan(0);
        assertThat(result).isEqualTo(response);

        verify(roomRepository).save(room);
    }

    @Test
    void willThrowWhenRoomCapacityIsLessOrEqualZeroOnCreate() {
        // given
        Room room = new Room();
        RoomRequest req = new RoomRequest(30, 0,COMPUTER_ROOM);

        given(mapper.toEntity(req))
                .willReturn(room);

        // when
        // then
        assertThatThrownBy(() -> underTest.createRoom(req))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Capacity must be greater than 0!");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void willThrowWhenRoomNumberIsLessOrEqualZeroOnCreate() {
        // given
        Room room = new Room();
        RoomRequest req = new RoomRequest(0,30, COMPUTER_ROOM);

        given(mapper.toEntity(req))
                .willReturn(room);

        // when
        // then
        assertThatThrownBy(() -> underTest.createRoom(req))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Room number must be positive!");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void shouldGetRoomById() {
        // given
        Room room = new Room(1L, 101, 30, COMPUTER_ROOM, null);
        RoomResponse response = new RoomResponse(1L, 101, 30, COMPUTER_ROOM);

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));
        given(mapper.toResponse(room))
                .willReturn(response);

        // when
        RoomResponse result = underTest.getRoomById(room.getId());

        // then
        assertThat(result).isEqualTo(response);

        verify(mapper).toResponse(room);
    }

    @Test
    void willThrowWhenRoomNotFoundOnGetById() {
        // given
        Long id = 1L;
        given(roomRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getRoomById(id))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining("Room does not exist!");

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void shouldDeleteRoom() {
        // given
        Room room = new Room(1L, 101, 30, COMPUTER_ROOM, null);
        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));

        // when
        underTest.deleteRoom(room.getId());

        // then
        verify(roomRepository).delete(room);
    }

    @Test
    void willThrowWhenRoomNotFoundOnDelete() {
        // given
        Room room = new Room();

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteRoom(room.getId()))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining("Room not found!");

        verify(roomRepository, never()).delete(any());
    }

    @Test
    void shouldEditRoom() {
        // given
        Room room = new Room();
        room.setRoomType(TEACHING_ROOM);
        room.setRoomNumber(1);
        room.setCapacity(10);
        RoomPatchRequest req = new RoomPatchRequest(101, 40, COMPUTER_ROOM);
        RoomResponse response = mock(RoomResponse.class);

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));
        given(mapper.toResponse(room))
                .willReturn(response);

        // when
        RoomResponse result = underTest.editRoom(req, room.getId());

        // then
        assertThat(room.getRoomType()).isEqualTo(req.getRoomType());
        assertThat(room.getRoomNumber()).isEqualTo(req.getRoomNumber());
        assertThat(room.getCapacity()).isEqualTo(req.getCapacity());
        assertThat(result).isEqualTo(response);

        verify(roomRepository).save(room);
    }

    @Test
    void willThrowWhenRoomNotFoundOnEdit() {
        // given
        Long id = 1L;
        RoomPatchRequest req = new RoomPatchRequest();
        given(roomRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.editRoom(req, id))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining("Room not found!");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void willThrowWhenRoomCapacityIsNotPositive() {
        // given
        Room room = new Room();
        room.setRoomType(TEACHING_ROOM);
        room.setRoomNumber(1);
        room.setCapacity(10);
        RoomPatchRequest req = new RoomPatchRequest(101, 0, COMPUTER_ROOM);

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));

        // when
        // then
        assertThatThrownBy(() -> underTest.editRoom(req, room.getId()))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Capacity must be greater than 0!");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void willThrowWhenRoomNumberIsNotPositive() {
        // given
        Room room = new Room();
        room.setRoomType(TEACHING_ROOM);
        room.setRoomNumber(1);
        room.setCapacity(10);
        RoomPatchRequest req = new RoomPatchRequest(0, 40, COMPUTER_ROOM);

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));

        // when
        // then
        assertThatThrownBy(() -> underTest.editRoom(req, room.getId()))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Room number must be positive!");

        verify(roomRepository, never()).save(any());
    }
}
