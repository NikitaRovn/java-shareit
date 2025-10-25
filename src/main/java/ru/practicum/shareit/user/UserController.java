package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRegisterDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    UserDto registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return UserMapper.mapFromUserToUserDto(userService.registerUser(userRegisterDto));
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId) {
        userUpdateDto.setId(userId);
        return UserMapper.mapFromUserToUserDto(userService.updateUser(userUpdateDto));
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId) {
        return UserMapper.mapFromUserToUserDto(userService.getUser(userId));
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
