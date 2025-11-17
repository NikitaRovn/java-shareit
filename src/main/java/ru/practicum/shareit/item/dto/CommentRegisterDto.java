package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class CommentRegisterDto {
    private String text;

    private Long itemId;

    private Long userId;
}
