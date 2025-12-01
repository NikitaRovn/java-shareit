package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidBookingTimeException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public InvalidBookingTimeException() {
        this.errors = List.of(
                new ErrorResponse("time", "Дата начала бронирования должна быть раньше даты окончания.")
        );
    }
}