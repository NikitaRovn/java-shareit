package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(ItemRegisterDto itemRegisterDto);

    Item updateItem(ItemUpdateDto itemUpdateDto);

    Item getItem(Long itemId);

    List<Item> getYourItems(Long userId);

    List<Item> searchItems(String text);
}
