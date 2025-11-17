package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(BookingRegisterDto bookingRegisterDto);

    Booking changeBookingStatus(BookingUpdateDto bookingUpdateDto);

    Booking getBooking(Long id, Long userId);

    List<Booking> getListYourBooking(Long id, String state);

    List<Booking> getListYourItemsBooking(Long id, String state);

    void deleteBooking(Long id);
}
