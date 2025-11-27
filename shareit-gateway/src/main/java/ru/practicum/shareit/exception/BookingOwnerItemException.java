package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BookingOwnerItemException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public BookingOwnerItemException(Long itemId) {
        this.errors = List.of(
                new ErrorResponse("item", "Вещь с id: " + itemId + " ваша, её нельзя бронировать.")
        );
    }
}
