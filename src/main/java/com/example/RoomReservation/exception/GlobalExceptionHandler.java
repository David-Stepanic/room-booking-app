package com.example.RoomReservation.exception;

import com.example.RoomReservation.exception.custom.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                401,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ErrorResponse> handleRoomException(RoomException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFound(RoomException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                404,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> handleReservationException(ReservationException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                404,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest().body(
                new ErrorResponse(message, 400, LocalDateTime.now())
        );
    }
}
