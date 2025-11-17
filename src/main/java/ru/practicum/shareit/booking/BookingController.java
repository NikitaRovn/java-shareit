package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingRegisterDto bookingRegisterDto,
                          @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        bookingRegisterDto.setBookerId(bookerId);
        return BookingMapper.mapFromBookingToBookingDto(bookingService.addBooking(bookingRegisterDto));
    }

    @PatchMapping("/{id}")
    public BookingDto changeBookingStatus(@RequestParam Boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                   @PathVariable Long id) {
        BookingUpdateDto bookingUpdateDto = BookingUpdateDto.builder()
                .id(id)
                .ownerId(ownerId)
                .approved(approved)
                .build();

        return BookingMapper.mapFromBookingToBookingDto(bookingService.changeBookingStatus(bookingUpdateDto));
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long id) {
        return BookingMapper.mapFromBookingToBookingDto(bookingService.getBooking(id, userId));
    }

    @GetMapping
    public List<BookingDto> getListYourBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapFromListBookingToListBookingDto(bookingService.getListYourBooking(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getListYourItemsBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapFromListBookingToListBookingDto(bookingService.getListYourItemsBooking(userId, state));
    }
}
