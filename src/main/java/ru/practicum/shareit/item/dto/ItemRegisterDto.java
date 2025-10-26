package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRegisterDto {
    @NotNull(message = "Поле name должно быть передано.")
    @NotBlank(message = "Поле name не должно быть пустой строкой или строкой из пробелов.")
    private String name;

    @NotNull(message = "Поле description должно быть передано.")
    @NotBlank(message = "Поле description не должно быть пустой строкой или строкой из пробелов.")
    private String description;

    @NotNull(message = "Поле available должно быть передано.")
    private Boolean available;

    private Long ownerId;
}
