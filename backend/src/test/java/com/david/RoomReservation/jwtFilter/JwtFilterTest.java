package com.david.RoomReservation.jwtFilter;

import com.david.RoomReservation.config.JwtFilter;
import com.david.RoomReservation.service.auth.JWTService;
import com.david.RoomReservation.service.auth.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private ApplicationContext context;
    @Mock
    private JWTService jwtService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private MyUserDetailsService userDetailsService;
    @Mock
    private UserDetails userDetails;

    private JwtFilter underTest;

    @BeforeEach
    void setUp() {
        underTest = new JwtFilter();
        underTest.context = context;
        underTest.jwtService = jwtService;
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws ServletException, IOException {
        // given
        String token = "valid token";
        String username = "test@gmail.com";

        given(request.getHeader("Authorization"))
                .willReturn("Bearer " + token);
        given(jwtService.extractUsername(token))
                .willReturn(username);
        given(context.getBean(MyUserDetailsService.class))
                .willReturn(userDetailsService);
        given(userDetailsService.loadUserByUsername(username))
                .willReturn(userDetails);
        given(jwtService.validateToken(token, userDetails))
                .willReturn(true);
        given(userDetails.getAuthorities()).willReturn(List.of());

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldSkipWhenNoAuthHeader() throws ServletException, IOException {
        // given
        given(request.getHeader("Authorization"))
                .willReturn(null);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
    }

    @Test
    void shouldSkipWhenAuthHeaderIsNotBearer() throws ServletException, IOException {
        // given
        given(request.getHeader("Authorization"))
                .willReturn("Test123");

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        // given
        String token = "Invalid token";
        String username = "test@gmail.com";

        given(request.getHeader("Authorization"))
                .willReturn("Bearer " + token);
        given(jwtService.extractUsername(token)).willReturn(username);
        given(context.getBean(MyUserDetailsService.class)).willReturn(userDetailsService);
        given(userDetailsService.loadUserByUsername(username)).willReturn(userDetails);
        given(jwtService.validateToken(token, userDetails)).willReturn(false);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
