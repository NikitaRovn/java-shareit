package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User saveUser(User user);

    User updateUser(User user);

    User findUserByUserId(Long userId);

    User findUserByUserEmail(String userEmail);

    void deleteUser(Long userId);
}
