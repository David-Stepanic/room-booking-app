package com.example.RoomReservation.model.constans;

import lombok.Getter;

/**
 *
 * @author PC
 */
@Getter
public enum Role {
    USER("User"),
    ADMIN("Admin");

    private final String label;

    Role(String label) {
        this.label = label;
    }

}
