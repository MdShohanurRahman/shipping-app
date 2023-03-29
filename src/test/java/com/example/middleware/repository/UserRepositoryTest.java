package com.example.middleware.repository;

import com.example.middleware.entity.User;
import com.example.middleware.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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


    @Test
    void testFindByEmail() {
        // given
        User user = this.userList.get(0);
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByEmailShouldReturnEmpty() {
        // when
        Optional<User> found = userRepository.findByEmail("non-existing-email@example.com");

        // then
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void testFindByUuid() {
        // given
        User user = this.userList.get(0);
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByUuid(user.getUuid());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUuid()).isEqualTo(user.getUuid());
    }

    @Test
    void testExistsByEmail() {
        // given
        User user = this.userList.get(0);
        userRepository.save(user);

        // when
        boolean found = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(found).isTrue();
    }
}