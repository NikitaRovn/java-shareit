package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BookingAlreadyApprovedException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public BookingAlreadyApprovedException(Long bookingId) {
        this.errors = List.of(
                new ErrorResponse("status", "Бронирование с id: " + bookingId + " уже подтверждено.")
        );
    }
}
