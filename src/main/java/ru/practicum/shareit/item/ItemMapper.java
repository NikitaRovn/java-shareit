package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto mapFromItemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static List<ItemDto> mapFromListItemToListItemDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::mapFromItemToItemDto)
                .toList();
    }

    public static Item mapFromItemRegisterDtoToItem(ItemRegisterDto itemRegisterDto) {
        User owner = User.builder()
                .id(itemRegisterDto.getOwnerId())
                .build();

        return Item.builder()
                .id(null)
                .name(itemRegisterDto.getName())
                .description(itemRegisterDto.getDescription())
                .available(itemRegisterDto.getAvailable())
                .owner(owner)
                .request(null)
                .build();
    }

    public static Item mapFromItemUpdateDtoToItem(ItemUpdateDto itemUpdateDto) {
        User owner = User.builder()
                .id(itemUpdateDto.getOwnerId())
                .build();

        return Item.builder()
                .id(itemUpdateDto.getId())
                .name(itemUpdateDto.getName())
                .description(itemUpdateDto.getDescription())
                .available(itemUpdateDto.getAvailable())
                .owner(owner)
                .request(null)
                .build();
    }
}
