package ru.practicum.shareit.user;

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
    public UserDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return UserMapper.mapFromUserToUserDto(userService.registerUser(userRegisterDto));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id) {
        return UserMapper.mapFromUserToUserDto(userService.updateUser(id, userUpdateDto));
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return UserMapper.mapFromUserToUserDto(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
