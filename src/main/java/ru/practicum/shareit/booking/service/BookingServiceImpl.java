package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        User owner = booking.getItem().getOwner();
        User booker = booking.getBooker();

        if (Objects.equals(user, owner) || Objects.equals(user, booker)) {
            return booking;
        }

        throw new BookingNotOwnerException(id);
    }

    @Override
    public List<Booking> getListYourBooking(Long id, String state) {
        return List.of();
    }

    @Override
    public List<Booking> getListYourItemsBooking(Long id, String state) {
        return List.of();
    }

    @Override
    public void deleteBooking(Long id) {

    }
}
