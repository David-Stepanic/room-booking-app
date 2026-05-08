package com.david.RoomReservation.model.constans;

import lombok.Getter;

/**
 *
 * @author PC
 */
@Getter
public enum RoomType {
    COMPUTER_ROOM("Computer Room"),
    TEACHING_ROOM("Teaching Room"),
    COMPUTER_CENTER("Computer Center"),
    AMPHITHEATER("Amphitheater");

    private final String label;

    RoomType(String label) {
        this.label = label;
    }

}
