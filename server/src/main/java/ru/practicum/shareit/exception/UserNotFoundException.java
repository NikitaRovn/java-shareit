package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public UserNotFoundException(Long userId) {
        this.errors = List.of(new ErrorResponse("user", "Пользователь с id: " + userId + " не существует."));
    }
}
