package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidBookingTimeException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRegisterDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (!Objects.equals(item.getOwner().getId(), itemUpdateDto.getOwnerId())) {
            throw new NotOwnerException(id);
        }

        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setIsAvailable(itemUpdateDto.getAvailable());
        }

        return itemRepository.save(item);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = getExistingItem(itemId);

        List<CommentDto> comments = commentRepository.findByItem_Id(itemId).stream()
                .map(CommentMapper::mapFromCommentToCommentDto)
                .toList();

        if (!item.getOwner().getId().equals(userId)) {
            return ItemMapper.mapFromItemToItemDto(item, null, null, comments);
        }

        LocalDateTime now = LocalDateTime.now();

        BookingShortDto last =
                bookingRepository.findFirstByItem_IdAndStartBeforeOrderByEndDesc(itemId, now)
                        .map(b -> new BookingShortDto(b.getId(), b.getBooker().getId()))
                        .orElse(null);

        BookingShortDto next =
                bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, now)
                        .map(b -> new BookingShortDto(b.getId(), b.getBooker().getId()))
                        .orElse(null);


        return ItemMapper.mapFromItemToItemDto(item, last, next, comments);
    }




    @Override
    public List<ItemDto> getYourItems(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(ownerId);
        }

        LocalDateTime now = LocalDateTime.now();

        List<Item> items = itemRepository.findByOwnerId(ownerId);

        return items.stream()
                .map(item -> {
                    Long itemId = item.getId();

                    BookingShortDto last =
                            bookingRepository.findFirstByItem_IdAndStartBeforeOrderByEndDesc(itemId, now)
                                    .map(b -> new BookingShortDto(b.getId(), b.getBooker().getId()))
                                    .orElse(null);

                    BookingShortDto next =
                            bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, now)
                                    .map(b -> new BookingShortDto(b.getId(), b.getBooker().getId()))
                                    .orElse(null);


                    List<CommentDto> comments = commentRepository.findByItem_Id(itemId).stream()
                            .map(CommentMapper::mapFromCommentToCommentDto)
                            .toList();

                    return ItemMapper.mapFromItemToItemDto(item, last, next, comments);
                })
                .toList();
    }

    @Override
    public List<Item> searchItems(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return itemRepository.search(query);
    }

    @Override
    public Comment addComment(CommentRegisterDto commentRegisterDto) {
        Long itemId = commentRegisterDto.getItemId();
        Item item = getExistingItem(itemId);

        Long userId = commentRegisterDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        LocalDateTime now = LocalDateTime.now();

        boolean hasFinishedApprovedBooking = bookingRepository
                .existsByItemAndBookerAndStatusAndEndBefore(item, user, StatusBooking.APPROVED, now);

        if (!hasFinishedApprovedBooking) {
            throw new InvalidBookingTimeException();
        }

        return commentRepository.save(
                CommentMapper.mapFromCommentRegisterDtoToComment(commentRegisterDto, item, user)
        );

    }

    private Item getExistingItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }
}
