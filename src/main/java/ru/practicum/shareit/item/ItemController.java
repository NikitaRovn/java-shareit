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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRegisterDto;
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
    public ItemDto addItem(@Valid @RequestBody ItemRegisterDto itemRegisterDto,
                    @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemRegisterDto.setOwnerId(ownerId);
        return ItemMapper.mapFromItemToItemDto(itemService.addItem(itemRegisterDto));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemUpdateDto itemUpdateDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable Long id) {
        itemUpdateDto.setId(id);
        itemUpdateDto.setOwnerId(userId);
        return ItemMapper.mapFromItemToItemDto(itemService.updateItem(id, itemUpdateDto));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getYourItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.mapFromListItemToListItemDto(itemService.getYourItems(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(required = false) String text) {
        return ItemMapper.mapFromListItemToListItemDto(itemService.searchItems(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentRegisterDto commentRegisterDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId) {
        commentRegisterDto.setUserId(userId);
        commentRegisterDto.setItemId(itemId);
        return CommentMapper.mapFromCommentToCommentDto(itemService.addComment(commentRegisterDto));
    }
}
