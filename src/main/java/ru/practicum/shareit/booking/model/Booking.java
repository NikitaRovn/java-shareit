package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class Booking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    StatusBooking status;
}
