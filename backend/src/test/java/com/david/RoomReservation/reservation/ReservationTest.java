package com.david.RoomReservation.reservation;

import com.david.RoomReservation.exception.custom.InvalidDateRangeException;
import com.david.RoomReservation.exception.custom.ReservationException;
import com.david.RoomReservation.model.Reservation;
import com.david.RoomReservation.model.constans.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReservationTest {

    @Test
    void defaultConstructorShouldSetPendingStatusAndCreatedAt() {
        // when
        Reservation reservation = new Reservation();

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(reservation.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldConfirmPendingReservation() {
        // given
        Reservation reservation = new Reservation();

        // when
        reservation.confirm();

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
    }

    @Test
    void willThrowWhenConfirmingNonPendingReservation() {
        // given
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CANCELED);

        // when
        // then
        assertThatThrownBy(reservation::confirm)
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be confirmed!");
    }

    @Test
    void shouldDeclinePendingReservationWithReason() {
        // given
        Reservation reservation = new Reservation();

        // when
        reservation.decline("Room needed for maintenance");

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.DECLINED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
        assertThat(reservation.getDeclinedReason()).isEqualTo("Room needed for maintenance");
    }

    @Test
    void willThrowWhenDecliningNonPendingReservation() {
        // given
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        // when
        // then
        assertThatThrownBy(() -> reservation.decline("some reason"))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be declined!");
    }

    @Test
    void willThrowWhenDeclineReasonIsNull() {
        // given
        Reservation reservation = new Reservation();

        // when
        // then
        assertThatThrownBy(() -> reservation.decline(null))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Decline reason is required!");
    }

    @Test
    void willThrowWhenDeclineReasonIsBlank() {
        // given
        Reservation reservation = new Reservation();

        // when
        // then
        assertThatThrownBy(() -> reservation.decline("   "))
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Decline reason is required!");
    }

    @Test
    void shouldCancelPendingReservation() {
        // given
        Reservation reservation = new Reservation();

        // when
        reservation.cancel();

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CANCELED);
        assertThat(reservation.getDecisionMadeAt()).isNotNull();
    }

    @Test
    void willThrowWhenCancelingNonPendingReservation() {
        // given
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.DECLINED);

        // when
        // then
        assertThatThrownBy(reservation::cancel)
                .isInstanceOf(ReservationException.class)
                .hasMessageContaining("Only pending reservations can be canceled!");
    }

    @Test
    void shouldScheduleValidTimeRange() {
        // given
        Reservation reservation = new Reservation();
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        // when
        reservation.schedule(start, end);

        // then
        assertThat(reservation.getStartTime()).isEqualTo(start);
        assertThat(reservation.getEndTime()).isEqualTo(end);
    }

    @Test
    void willThrowWhenStartTimeIsAfterEndTime() {
        // given
        Reservation reservation = new Reservation();
        LocalDateTime start = LocalDateTime.now().plusHours(3);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        // when
        // then
        assertThatThrownBy(() -> reservation.schedule(start, end))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("Start time must be before end time!");
    }

    @Test
    void willThrowWhenStartTimeEqualsEndTime() {
        // given
        Reservation reservation = new Reservation();
        LocalDateTime time = LocalDateTime.now().plusHours(1);

        // when
        // then
        reservation.schedule(time, time);

        assertThat(reservation.getStartTime()).isEqualTo(time);
        assertThat(reservation.getEndTime()).isEqualTo(time);
    }

    @Test
    void willThrowWhenStartTimeIsInThePast() {
        // given
        Reservation reservation = new Reservation();
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        // when
        // then
        assertThatThrownBy(() -> reservation.schedule(start, end))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("Start time cannot be in the past!");
    }
}
