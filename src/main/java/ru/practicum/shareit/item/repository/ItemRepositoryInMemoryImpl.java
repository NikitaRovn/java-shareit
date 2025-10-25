package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public Item saveItem(Item item) {
        Long newId = lastId++;
        item.setId(newId);
        items.put(newId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Long itemId = item.getId();
        Item currentItem = findItemByItemId(itemId);
        items.put(itemId, currentItem);
        return currentItem;
    }

    @Override
    public Item findItemByItemId(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(el -> el.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> findItemsByQuery(String query) {
        String q = query.toLowerCase();

        return items.values().stream()
                .filter(el -> el.getName().toLowerCase().contains(q)
                        || el.getDescription().toLowerCase().contains(q))
                .filter(el -> Boolean.TRUE.equals(el.getAvailable()))
                .toList();
    }
}
