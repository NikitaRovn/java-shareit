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

        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(ownerId);
        }

        return itemRepository.save(itemToSave);
    }

    @Override
    public Item updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        Item itemUpdated = ItemMapper.mapFromItemUpdateDtoToItem(itemUpdateDto);

        if (!Objects.equals(itemToUpdate.getOwner().getId(), itemUpdateDto.getOwnerId())) {
            throw new NotOwnerException(id);
        }

        if (itemUpdated.getName() != null) {
            itemToUpdate.setName(itemUpdated.getName());
        }
        if (itemUpdated.getDescription() != null) {
            itemToUpdate.setDescription(itemUpdated.getDescription());
        }
        if (itemUpdated.getIsAvailable() != null) {
            itemToUpdate.setIsAvailable(itemUpdated.getIsAvailable());
        }
        return itemRepository.save(itemToUpdate);
    }

    @Override
    public Item getItem(Long id) {
        return getExistingItem(id);
    }

    @Override
    public List<Item> getYourItems(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        return itemRepository.findByOwnerId(id);
    }

    @Override
    public List<Item> searchItems(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return itemRepository.search(query);
    }

    @Override
    public void deleteItem(Long id) {
        getExistingItem(id);
        itemRepository.deleteById(id);
    }

    private Item getExistingItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }
}
