package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.BookingNotOwnerException;
import ru.practicum.shareit.exception.BookingOwnerItemException;
import ru.practicum.shareit.exception.InvalidBookingTimeException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Booking addBooking(BookingRegisterDto bookingRegisterDto) {
        Long bookerId = bookingRegisterDto.getBookerId();
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException(bookerId));

        Long itemId = bookingRegisterDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        if (item.getOwner().getId().equals(booker.getId())) {
            throw new BookingOwnerItemException(itemId);
        }

        if (Boolean.FALSE.equals(item.getIsAvailable())) {
            throw new ItemUnavailableException(itemId);
        }

        LocalDateTime start = bookingRegisterDto.getStart();
        LocalDateTime end = bookingRegisterDto.getEnd();
        if (start == null || end == null || !start.isBefore(end)) {
            throw new InvalidBookingTimeException();
        }

        Booking bookingToSave = BookingMapper.mapFromBookingRegisterDtoToBooking(bookingRegisterDto);
        bookingToSave.setBooker(booker);
        bookingToSave.setItem(item);

        return bookingRepository.save(bookingToSave);
    }

    @Override
    public Booking changeBookingStatus(BookingUpdateDto bookingUpdateDto) {
        Long bookingId = bookingUpdateDto.getId();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        Long ownerId = bookingUpdateDto.getOwnerId();
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new NotOwnerException(ownerId);
        }

        if (booking.getStatus() == StatusBooking.APPROVED) {
            throw new BookingAlreadyApprovedException(bookingId);
        }

        if (bookingUpdateDto.getApproved()) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        if (Objects.equals(userId, ownerId) || Objects.equals(userId, bookerId)) {
            return booking;
        }

        throw new BookingNotOwnerException(id);
    }

    @Override
    public List<Booking> getListYourBooking(Long id, String state) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        StateBooking bookingState = StateBooking.from(state);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        return switch (bookingState) {
            case ALL -> bookingRepository.findByBooker_Id(id, sort);

            case CURRENT -> bookingRepository
                    .findByBooker_IdAndStartBeforeAndEndAfter(id, now, now, sort);

            case PAST -> bookingRepository
                    .findByBooker_IdAndEndBefore(id, now, sort);

            case FUTURE -> bookingRepository
                    .findByBooker_IdAndStartAfter(id, now, sort);

            case WAITING -> bookingRepository
                    .findByBooker_IdAndStatus(id, StatusBooking.WAITING, sort);

            case REJECTED -> bookingRepository
                    .findByBooker_IdAndStatus(id, StatusBooking.REJECTED, sort);
        };
    }

    @Override
    public List<Booking> getListYourItemsBooking(Long id, String state) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        StateBooking bookingState = StateBooking.from(state);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        return switch (bookingState) {
            case ALL -> bookingRepository.findByItem_Owner_Id(id, sort);

            case CURRENT -> bookingRepository
                    .findByItem_Owner_IdAndStartBeforeAndEndAfter(id, now, now, sort);

            case PAST -> bookingRepository
                    .findByItem_Owner_IdAndEndBefore(id, now, sort);

            case FUTURE -> bookingRepository
                    .findByItem_Owner_IdAndStartAfter(id, now, sort);

            case WAITING -> bookingRepository
                    .findByItem_Owner_IdAndStatus(id, StatusBooking.WAITING, sort);

            case REJECTED -> bookingRepository
                    .findByItem_Owner_IdAndStatus(id, StatusBooking.REJECTED, sort);
        };
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }
}
