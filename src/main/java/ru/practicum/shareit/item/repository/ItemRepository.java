package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item saveItem(Item item);

    Item updateItem(Item item);

    Item findItemByItemId(Long itemId);

    List<Item> findItemsByUserId(Long userId);

    List<Item> findItemsByQuery(String query);
}
