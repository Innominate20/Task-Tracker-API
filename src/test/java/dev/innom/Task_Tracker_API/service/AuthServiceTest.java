package dev.innom.Task_Tracker_API.service;

import dev.innom.Task_Tracker_API.dto.LoginRequest;
import dev.innom.Task_Tracker_API.jwt.JwtUtility;
import dev.innom.Task_Tracker_API.mapper.RegisterDtoMapper;
import dev.innom.Task_Tracker_API.repository.UserRepository;
import dev.innom.Task_Tracker_API.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success_ReturnToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(loginRequest.getEmail());
        when(jwtUtility.generateToke(loginRequest.getEmail())).thenReturn("jwt-token");

        ResponseEntity<String> response = authService.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody());
    }
}
