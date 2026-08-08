package com.david.RoomReservation.exception;

import com.david.RoomReservation.dto.error.ApiErrorResponse;
import com.david.RoomReservation.exception.custom.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler underTest = new GlobalExceptionHandler();

    @Test
    void shouldHandleInvalidDateRangeException() {
        // given
        InvalidDateRangeException ex = new InvalidDateRangeException("Invalid range");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleInvalidDate(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Invalid range");
        assertThat(result.getBody().getStatus()).isEqualTo(400);
    }

    @Test
    void shouldHandleMissingServletRequestParameterException() {
        // given
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("roomId", "Long");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleMissingParams(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Missing required parameter: roomId");
    }

    @Test
    void shouldHandleUsernameNotFoundException() {
        // given
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleUserNotFound(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("User not found");
        assertThat(result.getBody().getStatus()).isEqualTo(401);
    }

    @Test
    void shouldHandleRoomException() {
        // given
        RoomException ex = new RoomException("Capacity must be greater than 0!");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleRoomException(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Capacity must be greater than 0!");
    }

    @Test
    void shouldHandleRoomNotFoundException() {
        // given
        RoomNotFoundException ex = new RoomNotFoundException("Room not found!");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleRoomNotFound(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // napomena ispod
        assertThat(result.getBody().getMessage()).isEqualTo("Room not found!");
        assertThat(result.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldHandleReservationException() {
        // given
        ReservationException ex = new ReservationException("Room is already reserved in this time range");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleReservationException(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Room is already reserved in this time range");
    }

    @Test
    void shouldHandleReservationNotFoundException() {
        // given
        ReservationNotFoundException ex = new ReservationNotFoundException("Reservation does not exist!");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleReservationNotFound(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Reservation does not exist!");
        assertThat(result.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "email", "Invalid email format!");

        given(ex.getBindingResult()).willReturn(bindingResult);
        given(bindingResult.getFieldErrors()).willReturn(List.of(fieldError));

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleValidation(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Invalid email format!");
    }

    @Test
    void shouldHandleUserExistsException() {
        // given
        UserExistsException ex = new UserExistsException("Email already exists");

        // when
        ResponseEntity<ErrorResponse> result = underTest.handleUserExist(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Email already exists");
    }

    @Test
    void shouldHandleBadCredentialsException() {
        // given
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        // when
        ResponseEntity<ApiErrorResponse> result = underTest.handleBadCredentials(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result.getBody().getMessage()).isEqualTo("Invalid email or password");
        assertThat(result.getBody().getError()).isEqualTo("Unauthorized");
        assertThat(result.getBody().getStatus()).isEqualTo(401);
    }

    @Test
    void shouldHandleVerifyMailException() {
        // given
        VerifyMailException ex = new VerifyMailException("Token expired", "TOKEN_EXPIRED");

        // when
        ResponseEntity<Map<String, String>> result = underTest.handle(ex);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().get("message")).isEqualTo("Token expired");
        assertThat(result.getBody().get("code")).isEqualTo("TOKEN_EXPIRED");
    }

    @Test
    void shouldHandleChangePasswordException() {
        // given
        ChangePasswordException ex = new ChangePasswordException("Old password is incorrect");

        // when
        String result = underTest.handle(ex);

        // then
        assertThat(result).isEqualTo("Old password is incorrect");
    }

    @Test
    void shouldHandleTokenException() {
        // given
        TokenException ex = new TokenException("Invalid token");

        // when
        String result = underTest.handle(ex);

        // then
        assertThat(result).isEqualTo("Invalid token");
    }
}