package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemUnavailableException extends RuntimeException {
  private final List<ErrorResponse> errors;

  public ItemUnavailableException(Long itemId) {
    this.errors = List.of(
            new ErrorResponse("item", "Вещь с id: " + itemId + " недоступна для бронирования.")
    );
  }
}
