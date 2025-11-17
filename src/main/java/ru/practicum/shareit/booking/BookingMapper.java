package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDto mapFromBookingToBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .build();
    }

    public static List<BookingDto> mapFromListBookingToListBookingDto(List<Booking> bookingList) {
        return bookingList.stream().map(BookingMapper::mapFromBookingToBookingDto).toList();
    }

    public static Booking mapFromBookingRegisterDtoToBooking(BookingRegisterDto bookingRegisterDto) {
        Item item = Item.builder()
                .id(bookingRegisterDto.getItemId())
                .build();
        User user = User.builder()
                .id(bookingRegisterDto.getBookerId())
                .build();
        return Booking.builder()
                .start(bookingRegisterDto.getStart())
                .end(bookingRegisterDto.getEnd())
                .item(item)
                .booker(user)
                .status(StatusBooking.WAITING)
                .build();
    }
}
