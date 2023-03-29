package com.example.middleware.service;

import com.example.middleware.entity.User;
import com.example.middleware.enums.Role;
import com.example.middleware.exception.ApiException;
import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;
import com.example.middleware.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

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
    public void testGetUsers() {
        // given
        List<User> userList = this.userList;

        // when
        when(userRepository.findAll()).thenReturn(userList);
        List<UserResponse> result = userService.getUsers();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat("a@gmai.com").isEqualTo(result.get(0).email());
        assertThat("b@gmai.com").isEqualTo(result.get(1).email());
    }

    @Test
    public void testUpdateUserByUUID() {
        // Given
        User user = this.userList.get(0);
        UserRequest userRequest = new UserRequest("example@gmail.com");

        // When
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        user.setEmail("example@gmail.com");
        UserResponse userResponse = userService.updateUserByUUID(user.getUuid(), userRequest);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(userResponse.email()).isEqualTo(user.getEmail());
    }

    @Test
    void testGetUserByUUID() {
        // given
        User user = this.userList.get(0);

        // when
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));

        // then
        var userByUUID = userService.getUserByUUID(user.getUuid());
        assertThat(userByUUID).isNotNull();
    }

    @Test
    void testUpdateUserByUUIDWhenUserNotFound() {
        // given
        User user = this.userList.get(0);
        UserRequest request = new UserRequest("abc@gmail.com");

        // when
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.updateUserByUUID(user.getUuid(), request))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith("User not found");

        // then
        verify(userRepository, times(1)).findByUuid(user.getUuid());
    }

    @Test
    public void testDeleteUserByUUID() {
        // Given
        User user = this.userList.get(0);

        // When
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        userService.deleteUserByUUID(user.getUuid());

        // Then
        verify(userRepository, times(1)).delete(user);
    }
}