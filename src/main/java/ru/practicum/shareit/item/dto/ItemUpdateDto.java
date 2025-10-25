package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {
    Long id;

    String name;

    String description;

    Boolean available;

    Long ownerId;
}
