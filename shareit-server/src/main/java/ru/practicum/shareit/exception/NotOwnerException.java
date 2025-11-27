package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class NotOwnerException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public NotOwnerException(Long itemId) {
        this.errors = List.of(new ErrorResponse("item", "Предмет с id: " + itemId + " вам не принадлежит."));
    }
}
