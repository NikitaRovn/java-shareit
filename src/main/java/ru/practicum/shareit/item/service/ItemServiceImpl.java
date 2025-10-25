package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(ItemRegisterDto itemRegisterDto) {
        Item itemToSave = ItemMapper.mapFromItemRegisterDtoToItem(itemRegisterDto);
        Long ownerId = itemToSave.getOwner().getId();
        User owner = userRepository.findUserByUserId(ownerId);
        if (owner == null) throw new UserNotFoundException(ownerId);

        return itemRepository.saveItem(itemToSave);
    }

    @Override
    public Item updateItem(ItemUpdateDto itemUpdateDto) {
        Long itemId = itemUpdateDto.getId();
        Item itemToUpdate = itemRepository.findItemByItemId(itemId);
        if (itemToUpdate == null) throw new ItemNotFoundException(itemId);

        if (!Objects.equals(itemToUpdate.getOwner().getId(), itemUpdateDto.getOwnerId())) {
            throw new NotOwnerException(itemId);
        }

        if (itemUpdateDto.getName() != null) {
            itemToUpdate.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            itemToUpdate.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemUpdateDto.getAvailable());
        }

        return itemRepository.updateItem(itemToUpdate);
    }

    @Override
    public Item getItem(Long itemId) {
        Item item = itemRepository.findItemByItemId(itemId);
        if (item == null) throw new ItemNotFoundException(itemId);
        return item;
    }

    @Override
    public List<Item> getYourItems(Long userId) {
        return itemRepository.findItemsByUserId(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository.findItemsByQuery(text);
    }
}
