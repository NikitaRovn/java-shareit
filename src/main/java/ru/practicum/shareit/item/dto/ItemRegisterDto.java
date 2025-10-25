package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRegisterDto {
    @NotNull(message = "Поле name должно быть передано.")
    @NotBlank(message = "Поле name не должно быть пустой строкой или строкой из пробелов.")
    String name;

    @NotNull(message = "Поле description должно быть передано.")
    @NotBlank(message = "Поле description не должно быть пустой строкой или строкой из пробелов.")
    String description;

    @NotNull(message = "Поле available должно быть передано.")
    Boolean available;

    Long ownerId;
}
