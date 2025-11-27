package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidStateBookingException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public InvalidStateBookingException(String state) {
        this.errors = List.of(
                new ErrorResponse("state", "Неизвестный state запрос: " + state)
        );
    }
}
