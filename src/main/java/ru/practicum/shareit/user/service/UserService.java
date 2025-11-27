package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRegisterDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    User registerUser(UserRegisterDto userRegisterDto);

    User updateUser(Long id, UserUpdateDto userUpdateDto);

    User getUser(Long id);

    void deleteUser(Long id);
}
