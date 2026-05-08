package com.david.RoomReservation.model.constans;

import lombok.Getter;

/**
 *
 * @author PC
 */
@Getter
public enum ReservationStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    DECLINED("Declined"),
    CANCELED("Canceled");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

}
