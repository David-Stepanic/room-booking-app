package com.david.RoomReservation.room;

import com.david.RoomReservation.exception.custom.RoomException;
import com.david.RoomReservation.model.Room;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomTest {

    @Test
    void shouldUpdateCapacityWhenValid() {
        // given
        Room room = new Room();

        // when
        room.updateCapacity(50);

        // then
        assertThat(room.getCapacity()).isEqualTo(50);
    }

    @Test
    void willThrowWhenCapacityIsZero() {
        // given
        Room room = new Room();

        // when
        // then
        assertThatThrownBy(() -> room.updateCapacity(0))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Capacity must be greater than 0!");
    }

    @Test
    void willThrowWhenCapacityIsNegative() {
        // given
        Room room = new Room();

        // when
        // then
        assertThatThrownBy(() -> room.updateCapacity(-5))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Capacity must be greater than 0!");
    }

    @Test
    void shouldUpdateRoomNumberWhenValid() {
        // given
        Room room = new Room();

        // when
        room.updateRoomNumber(101);

        // then
        assertThat(room.getRoomNumber()).isEqualTo(101);
    }

    @Test
    void willThrowWhenRoomNumberIsZero() {
        // given
        Room room = new Room();

        // when
        // then
        assertThatThrownBy(() -> room.updateRoomNumber(0))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Room number must be positive!");
    }

    @Test
    void willThrowWhenRoomNumberIsNegative() {
        // given
        Room room = new Room();

        // when
        // then
        assertThatThrownBy(() -> room.updateRoomNumber(-10))
                .isInstanceOf(RoomException.class)
                .hasMessageContaining("Room number must be positive!");
    }
}