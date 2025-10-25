package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemNotFoundException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public ItemNotFoundException(Long itemId) {
        this.errors = List.of(new ErrorResponse("item", "Предмет с id: " + itemId + " не существует."));
    }
}
