package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {
    @NotNull(message = "Поле id должно быть передано.")
    Long id;

    String name;

    @NotNull(message = "Поле email должно быть передано.")
    @NotBlank(message = "Поле email не должно быть пустой строкой или строкой из пробелов.")
    @Email(message = "Поле email должно быть в корректном формате.")
    String email;
}
