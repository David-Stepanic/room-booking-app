package com.david.RoomReservation.auth;

import com.david.RoomReservation.dto.email.ForgotPasswordRequest;
import com.david.RoomReservation.dto.email.ResetPasswordRequest;
import com.david.RoomReservation.dto.user.LoginRequest;
import com.david.RoomReservation.dto.user.LoginResponse;
import com.david.RoomReservation.dto.user.RegisterRequest;
import com.david.RoomReservation.dto.user.RegisterResponse;
import com.david.RoomReservation.exception.custom.TokenException;
import com.david.RoomReservation.exception.custom.UserExistsException;
import com.david.RoomReservation.exception.custom.VerifyMailException;
import com.david.RoomReservation.mapper.UserMapper;
import com.david.RoomReservation.model.User;
import com.david.RoomReservation.model.auth.PasswordResetToken;
import com.david.RoomReservation.model.auth.UserPrincipal;
import com.david.RoomReservation.model.auth.VerificationToken;
import com.david.RoomReservation.model.constans.Department;
import com.david.RoomReservation.model.constans.Role;
import com.david.RoomReservation.repository.PasswordResetTokenRepository;
import com.david.RoomReservation.repository.UserRepository;
import com.david.RoomReservation.repository.VerificationTokenRepository;
import com.david.RoomReservation.service.auth.AuthService;
import com.david.RoomReservation.service.auth.JWTService;
import com.david.RoomReservation.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.david.RoomReservation.model.constans.Department.SOFTWARE_ENGINEERING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Mock
    AuthenticationManager authManager;
    @Mock
    private UserRepository repo;
    @Mock
    private VerificationTokenRepository tokenRepository;
    @Mock
    private PasswordResetTokenRepository resetRepo;
    @Mock
    private JWTService jwtService;
    @Mock
    private EmailService emailService;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private AuthService underTest;


    @Test
    void shouldRegisterUser() {
        // given
        RegisterRequest request = new RegisterRequest(
                "test@gmail.com",
                "Test",
                "Testovic",
                "Password123!",
                Department.SOFTWARE_ENGINEERING
        );

        RegisterResponse response = mock(RegisterResponse.class);

        given(repo.existsByEmail(request.getEmail()))
                .willReturn(false);
        given(mapper.toRegisterResponse(any(User.class)))
                .willReturn(response);

        // when
        RegisterResponse result = underTest.register(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(capturedUser.isEnabled()).isFalse();
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);
        assertThat(capturedUser.getPassword()).isNotEqualTo(request.getPassword());

        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        VerificationToken capturedToken = tokenCaptor.getValue();

        assertThat(capturedToken.getUser()).isEqualTo(capturedUser);

        verify(emailService).sendVerificationEmail(capturedToken);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void willThrowWhenUserAlreadyExistOnRegister() {
        // given
        RegisterRequest req = new RegisterRequest(
                "test@gmail.com",
                "Test",
                "Testovic",
                "Password123!",
                Department.SOFTWARE_ENGINEERING);

        given(repo.existsByEmail(any()))
                .willReturn(true);

        // when
        // then

        assertThatThrownBy(() -> underTest.register(req))
                .isInstanceOf(UserExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(repo, never()).save(any());
    }

    @Test
    void shouldVerifyUserEmail() {
        // given
        String tokenValue = "testToken";
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,false);
        VerificationToken token = new VerificationToken(tokenValue, user, LocalDateTime.now().plusHours(2));

        given(tokenRepository.findByToken(tokenValue))
                .willReturn(Optional.of(token));

        // when
        underTest.verifyEmail(tokenValue);

        // then
        assertThat(user.isEnabled()).isTrue();
        verify(repo).save(user);
    }

    @Test
    void willThrowWhenTokenNotFoundOnVerifyEmail() {
        // given
        String tokenValue = "testToken";

        given(tokenRepository.findByToken(tokenValue))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.verifyEmail(tokenValue))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("Invalid or expired verification link");

        verify(repo, never()).save(any());
    }

    @Test
    void willThrowWhenExpiryDateIsInPastOnVerifyEmail() {
        // given
        String tokenValue = "testToken";
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,false);
        VerificationToken token = new VerificationToken(tokenValue, user, LocalDateTime.now().minusHours(2));

        given(tokenRepository.findByToken(tokenValue))
                .willReturn(Optional.of(token));

        // when
        // then
        assertThatThrownBy(() -> underTest.verifyEmail(tokenValue))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("Token expired");

        verify(repo, never()).save(any());
    }

    @Test
    void willThrowWhenUserIsAlreadyEnabledOnVerifyEmail() {
        // given
        String tokenValue = "testToken";
        User user = new User ("test@gmail.com", "test", "test", "test123", Role.USER, SOFTWARE_ENGINEERING,true);
        VerificationToken token = new VerificationToken(tokenValue, user, LocalDateTime.now().plusHours(2));

        given(tokenRepository.findByToken(tokenValue))
                .willReturn(Optional.of(token));

        // when
        // then
        assertThatThrownBy(() -> underTest.verifyEmail(tokenValue))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("Account already verified");

        verify(repo, never()).save(any());
    }

    @Test
    void shouldLoginUser() {
        // given
        LoginRequest req = new LoginRequest("test@gmail.com", "Password123!");
        Authentication authentication = mock(Authentication.class);
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, true);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        given(authentication.getPrincipal()).willReturn(userPrincipal);
        given(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        LoginResponse response = new LoginResponse();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().toString());

        given(mapper.toLoginResponse(user))
                .willReturn(response);

        given(jwtService.generateToken(user.getEmail()))
                .willReturn("fake-jwt-token");

        // when
        LoginResponse result = underTest.login(req);

        // then
        assertThat(result.getToken()).isEqualTo("fake-jwt-token");
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void willThrowWhenAuthenticationFailOnLogin() {
        // given
        LoginRequest req = new LoginRequest("test@gmail.com", "Password123!");

        given(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Bad credentials"));

        // when
        // then
        assertThatThrownBy(() -> underTest.login(req))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void willThrowWhenUserNotEnabledOnLogin() {
        // given
        LoginRequest req = new LoginRequest("test@gmail.com", "Password123!");
        Authentication authentication = mock(Authentication.class);
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, false);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        given(authentication.getPrincipal()).willReturn(userPrincipal);
        given(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        // when
        // then
        assertThatThrownBy(() -> underTest.login(req))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("Verify your email first");

        verify(mapper, never()).toLoginResponse(any());
    }

    @Test
    void shouldResendVerificationEmail() {
        // given
        String email = "test@gmail.com";
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, false);

        given(repo.findByEmail(email))
                .willReturn(Optional.of(user));

        // when
        underTest.resendVerificationEmail(email);

        // then
        verify(tokenRepository).deleteByUser(user);

        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        VerificationToken capturedToken = tokenCaptor.getValue();

        assertThat(capturedToken.getUser()).isEqualTo(user);
        assertThat(capturedToken.getToken()).isNotBlank();
        assertThat(capturedToken.getExpiryDate()).isAfter(LocalDateTime.now());

        verify(emailService).sendVerificationEmail(capturedToken);
    }

    @Test
    void willThrowWhenUserNotFoundOnVerificationEmail() {
        // given
        String email = "test@gmail.com";

        given(repo.findByEmail(email))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.resendVerificationEmail(email))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("User not found");

        verify(tokenRepository, never()).deleteByUser(any());
    }

    @Test
    void willThrowWhenUserIsAlreadyVerifiedOnResendVerificationEmail() {
        // given
        String email = "test@gmail.com";
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, true);

        given(repo.findByEmail(email))
                .willReturn(Optional.of(user));

        // when
        // then
        assertThatThrownBy(() -> underTest.resendVerificationEmail(email))
                .isInstanceOf(VerifyMailException.class)
                .hasMessageContaining("User already verified");

        verify(tokenRepository, never()).deleteByUser(any());
    }

    @Test
    void shouldSendForgotPasswordMail() {
        // given
        ForgotPasswordRequest req = new ForgotPasswordRequest("test@gmail.com");
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, false);

        given(repo.findByEmail(req.email()))
                .willReturn(Optional.of(user));

        // when
        underTest.forgotPassword(req);

        // then
        verify(resetRepo).deleteByUser(user);

        ArgumentCaptor<PasswordResetToken> argumentCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(resetRepo).save(argumentCaptor.capture());
        PasswordResetToken capturedToken = argumentCaptor.getValue();

        assertThat(capturedToken.getToken()).isNotNull();
        assertThat(capturedToken.getUser()).isEqualTo(user);
        assertThat(capturedToken.getExpiryDate()).isAfter(LocalDateTime.now());

        verify(emailService).sendResetPasswordEmail(user.getEmail(), capturedToken.getToken());
    }

    @Test
    void willThrowWhenUserNotFoundOnSendForgotPasswordMail() {
        // given
        ForgotPasswordRequest req = new ForgotPasswordRequest("test@gmail.com");

        given(repo.findByEmail(req.email()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.forgotPassword(req))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(resetRepo, never()).deleteByUser(any());
    }

    @Test
    void shouldResetPassword() {
        // given
        ResetPasswordRequest req = new ResetPasswordRequest("testToken", "newPass");
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, false);
        PasswordResetToken resetToken = new PasswordResetToken(req.token(), user, LocalDateTime.now().plusMinutes(30));

        given(resetRepo.findByToken(req.token()))
                .willReturn(Optional.of(resetToken));

        // when
        underTest.resetPassword(req);

        // then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser.getPassword()).isNotEqualTo("encodedPass");
        assertThat(capturedUser.getPassword()).isNotEqualTo(req.newPassword());
        assertThat(capturedUser).isEqualTo(user);

        verify(resetRepo).delete(resetToken);
    }

    @Test
    void willThrowWhenNotFoundByTokenOnResetPassword() {
        // given
        ResetPasswordRequest req = new ResetPasswordRequest("testToken", "newPass");

        given(resetRepo.findByToken(req.token()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(req))
                .isInstanceOf(TokenException.class)
                .hasMessageContaining("Invalid token");

        verify(repo, never()).save(any());
    }

    @Test
    void willThrowWhenTokenExpiredOnResetPassword() {
        // given
        ResetPasswordRequest req = new ResetPasswordRequest("testToken", "newPass");
        User user = new User("test@gmail.com", "Test", "Testovic", "encodedPass", Role.USER, Department.SOFTWARE_ENGINEERING, false);
        PasswordResetToken resetToken = new PasswordResetToken(req.token(), user, LocalDateTime.now().minusMinutes(30));

        given(resetRepo.findByToken(req.token()))
                .willReturn(Optional.of(resetToken));

        // when
        // then

        assertThatThrownBy(() -> underTest.resetPassword(req))
                .isInstanceOf(TokenException.class)
                .hasMessageContaining("Token expired");

        verify(repo, never()).save(any());
    }
}
