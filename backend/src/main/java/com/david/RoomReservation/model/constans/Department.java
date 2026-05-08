package com.david.RoomReservation.model.constans;

import lombok.Getter;

@Getter
public enum Department {
    SOFTWARE_ENGINEERING("Software Engineering"),
    MARKETING("Marketing"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    MANAGEMENT("Management"),
    HUMAN_RESOURCES("Human Resources");

    private final String label;

    Department(String label) {
        this.label = label;
    }

}
