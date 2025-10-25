package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserFoundException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public UserFoundException(String email) {
        this.errors = List.of(new ErrorResponse("user", "Пользователь с email: " + email + " не существует."));
    }
}
