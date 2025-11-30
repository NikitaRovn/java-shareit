package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserRegisterDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void registerUser_success() {
        UserRegisterDto dto = UserRegisterDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        User user = userService.registerUser(dto);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
    }

    @Test
    void registerUser_duplicateEmail_throwsException() {
        UserRegisterDto dto = UserRegisterDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        userService.registerUser(dto);

        UserRegisterDto dto2 = UserRegisterDto.builder()
                .name("user2")
                .email("user@mail.com")
                .build();

        assertThatThrownBy(() -> userService.registerUser(dto2))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}
