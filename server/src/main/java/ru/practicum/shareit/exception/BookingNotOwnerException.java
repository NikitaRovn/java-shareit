package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BookingNotOwnerException extends RuntimeException {
  private final List<ErrorResponse> errors;

  public BookingNotOwnerException(Long bookingId) {
    this.errors = List.of(
            new ErrorResponse("item", "Бронирование с id: " + bookingId + " вам не принадлежит.")
    );
  }
}
