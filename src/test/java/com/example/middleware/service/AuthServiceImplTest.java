package com.example.middleware.service;

import com.example.middleware.entity.User;
import com.example.middleware.enums.Role;
import com.example.middleware.jwt.JwtAuthenticationProvider;
import com.example.middleware.jwt.JwtUtils;
import com.example.middleware.model.LoginRequest;
import com.example.middleware.model.TokenResponse;
import com.example.middleware.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    JwtAuthenticationProvider jwtAuthenticationProvider;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        userList = Arrays.asList(
                new User(
                        1L,
                        UUID.randomUUID().toString(),
                        "a@gmai.com", "1234",
                        Role.ROLE_ADMIN,
                        true
                ),
                new User(
                        2L, UUID.randomUUID().toString(),
                        "b@gmai.com",
                        "1234",
                        Role.ROLE_ADMIN, true
                )
        );
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
    }

    @Test
    void testFindUserByEmail() {
        // given
        User user = this.userList.get(0);

        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        var userByEmail = authService.findUserByEmail(user.getEmail());

        // then
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        assertThat(user).isEqualTo(userByEmail.get());
    }

    @Test
    public void testGetJwtToken() {
        // given
        LoginRequest loginRequest = new LoginRequest("admin@gmail.com", "admin");

        // Mock the authentication provider
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        Authentication authentication = mock(Authentication.class);
        when(jwtAuthenticationProvider.authenticate(authenticationToken)).thenReturn(authentication);

        // Mock the JWT token
        String token = "12354789@#$%@#@3e@D";

        // when
        when(jwtUtils.generateToken(authentication.getName())).thenReturn(token);

        // then
        TokenResponse tokenResponse = authService.getJwtToken(loginRequest);
        assertEquals(token, tokenResponse.token());
    }
}