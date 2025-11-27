package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.InvalidStateBookingException;

public enum StateBooking {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static StateBooking from(String value) {
        try {
            return StateBooking.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStateBookingException(value);
        }
    }
}
