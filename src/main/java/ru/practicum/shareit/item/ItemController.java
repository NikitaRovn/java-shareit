package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    ItemDto addItem(@Valid @RequestBody ItemRegisterDto itemRegisterDto,
                    @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemRegisterDto.setOwnerId(ownerId);
        return ItemMapper.mapFromItemToItemDto(itemService.addItem(itemRegisterDto));
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestBody ItemUpdateDto itemUpdateDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable Long itemId) {
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setOwnerId(userId);
        return ItemMapper.mapFromItemToItemDto(itemService.updateItem(itemUpdateDto));
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long itemId) {
        return ItemMapper.mapFromItemToItemDto(itemService.getItem(itemId));
    }

    @GetMapping
    List<ItemDto> getYourItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.mapFromListItemToListItemDto(itemService.getYourItems(userId));
    }

    @GetMapping("/search")
    List<ItemDto> searchItems(@RequestParam String text) {
        return ItemMapper.mapFromListItemToListItemDto(itemService.searchItems(text));
    }
}
