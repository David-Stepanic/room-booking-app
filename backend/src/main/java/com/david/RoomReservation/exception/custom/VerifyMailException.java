package com.david.RoomReservation.exception.custom;

public class VerifyMailException extends RuntimeException {

    private final String code;

    public VerifyMailException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
