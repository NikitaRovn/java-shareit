package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public UserAlreadyExistsException(String email) {
        this.errors = List.of(new ErrorResponse("user", "Пользователь с email: " + email + " уже существует."));
    }
}
