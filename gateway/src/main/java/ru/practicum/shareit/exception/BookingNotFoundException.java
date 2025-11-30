package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BookingNotFoundException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public BookingNotFoundException(Long bookingId) {
        this.errors = List.of(new ErrorResponse("user", "Бронирование с id: " + bookingId + " не существует."));
    }
}
