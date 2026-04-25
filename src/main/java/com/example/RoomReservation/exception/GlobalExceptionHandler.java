package com.example.RoomReservation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidDateRangeException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {

        ErrorResponse error = new ErrorResponse(
                "Missing required parameter: " + ex.getParameterName(),
                400,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }


}
