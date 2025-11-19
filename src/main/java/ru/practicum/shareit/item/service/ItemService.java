package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRegisterDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(ItemRegisterDto itemRegisterDto);

    Item updateItem(Long id, ItemUpdateDto itemUpdateDto);

    ItemDto getItem(Long itemId, Long userId);

    List<Item> getYourItems(Long id);

    List<Item> searchItems(String text);

    Comment addComment(CommentRegisterDto commentRegisterDto);
}
