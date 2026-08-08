package com.david.RoomReservation.user;

import com.david.RoomReservation.dto.user.PasswordRequest;
import com.david.RoomReservation.exception.custom.ChangePasswordException;
import com.david.RoomReservation.mapper.UserMapper;
import com.david.RoomReservation.model.User;
import com.david.RoomReservation.model.constans.Role;
import com.david.RoomReservation.repository.UserRepository;
import com.david.RoomReservation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.david.RoomReservation.model.constans.Department.SOFTWARE_ENGINEERING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserMapper mapper;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(repository, mapper, encoder);
    }

    @Test
    void shouldDeleteUser() {
        // given
        Long id = 1L;
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);

        given(repository.findById(id))
                .willReturn(Optional.of(user));

        // when
        underTest.deleteUser(id);

        // then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).delete(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
        verify(repository).delete(user);
    }

    @Test
    void willThrowWhenUserIsNotFoundOnDeleteUser() {
        // given
        Long id = 1L;
        given(repository.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found!");
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldChangePassword() {
        // given
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        PasswordRequest req = new PasswordRequest(user.getPassword(), "new123");

        given(repository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));
        given(encoder.matches(req.getOldPassword(), user.getPassword()))
                .willReturn(true);

        // when
        underTest.changePassword(user.getEmail(), req);

        // then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(encoder).encode(argumentCaptor.capture());
        String capturedPassword = argumentCaptor.getValue();

        assertThat(req.getNewPassword()).isEqualTo(capturedPassword);
        assertThat(user.getPassword()).isNotEqualTo(capturedPassword);
    }

    @Test
    void willThrowWhenUserIsNotFoundOnChangePassword() {
        // given
        String email = "test@gmail.com";
        PasswordRequest req = new PasswordRequest("old", "new");

        given(repository.findByEmail(email))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.changePassword(email, req))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found!");

        verify(encoder, never()).matches(any(), any());
    }

    @Test
    void willThrowWhenOldPasswordIsIncorrectOnChangePassword() {
        // given
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        PasswordRequest req = new PasswordRequest("test321", "new123");

        given(repository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        // when
        // then
        assertThatThrownBy(() -> underTest.changePassword(user.getEmail(), req))
                .isInstanceOf(ChangePasswordException.class)
                .hasMessageContaining("Old password is incorrect");

        verify(encoder, never()).encode(any());
    }

    @Test
    void willThrowWhenNewPasswordIsSame() {
        // given
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        PasswordRequest req = new PasswordRequest(user.getPassword(), "test123");

        given(repository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));
        given(encoder.matches(req.getOldPassword(), user.getPassword()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.changePassword(user.getEmail(), req))
                .isInstanceOf(ChangePasswordException.class)
                .hasMessageContaining("New password must be different");

        verify(encoder, never()).encode(any());
    }


}
