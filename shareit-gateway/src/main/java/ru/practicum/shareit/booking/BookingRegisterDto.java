package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRegisterDto {

    @NotNull(message = "Дата начала бронирования обязательна.")
    @FutureOrPresent(message = "Дата начала не может быть в прошлом.")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования обязательна.")
    @FutureOrPresent(message = "Дата окончания не может быть в прошлом.")
    private LocalDateTime end;

    @NotNull(message = "itemId обязателен.")
    private Long itemId;
}