package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto mapFromItemToItemDto(Item item) {
        if (item == null) return null;

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .build();
    }


    public static ItemDto mapFromItemToItemDto(
            Item item,
            BookingShortDto lastBooking,
            BookingShortDto nextBooking,
            List<CommentDto> comments
    ) {
        if (item == null) return null;

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static List<ItemDto> mapFromListItemToListItemDto(List<Item> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream()
                .map(ItemMapper::mapFromItemToItemDto)
                .toList();
    }

    public static Item mapFromItemRegisterDtoToItem(ItemRegisterDto itemRegisterDto) {
        if (itemRegisterDto == null) {
            return new Item();
        }

        User owner = User.builder()
                .id(itemRegisterDto.getOwnerId())
                .build();

        return Item.builder()
                .name(itemRegisterDto.getName())
                .description(itemRegisterDto.getDescription())
                .isAvailable(itemRegisterDto.getAvailable())
                .owner(owner)
                .build();
    }

    public static Item mapFromItemUpdateDtoToItem(ItemUpdateDto itemUpdateDto) {
        if (itemUpdateDto == null) {
            return new Item();
        }

        User owner = User.builder()
                .id(itemUpdateDto.getOwnerId())
                .build();

        return Item.builder()
                .id(itemUpdateDto.getId())
                .name(itemUpdateDto.getName())
                .description(itemUpdateDto.getDescription())
                .isAvailable(itemUpdateDto.getAvailable())
                .owner(owner)
                .build();
    }
}
