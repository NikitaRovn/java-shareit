package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingOwnerItemException;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addBooking_success() {
        User owner = userRepository.save(User.builder()
                .name("owner")
                .email("owner@mail.com")
                .build());

        User booker = userRepository.save(User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build());

        Item item = itemService.addItem(ItemRegisterDto.builder()
                .name("Вещь")
                .description("описание")
                .available(true)
                .ownerId(owner.getId())
                .build());

        BookingRegisterDto dto = BookingRegisterDto.builder()
                .itemId(item.getId())
                .bookerId(booker.getId())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();

        Booking booking = bookingService.addBooking(dto);

        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getItem().getId()).isEqualTo(item.getId());
        assertThat(booking.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(booking.getStatus()).isEqualTo(StatusBooking.WAITING);
    }

    @Test
    void addBooking_ownItem_throwsBookingOwnerItemException() {
        User owner = userRepository.save(User.builder()
                .name("owner")
                .email("owner@mail.com")
                .build());

        Item item = itemService.addItem(ItemRegisterDto.builder()
                .name("Вещь")
                .description("описание")
                .available(true)
                .ownerId(owner.getId())
                .build());

        BookingRegisterDto dto = BookingRegisterDto.builder()
                .itemId(item.getId())
                .bookerId(owner.getId())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();

        assertThatThrownBy(() -> bookingService.addBooking(dto))
                .isInstanceOf(BookingOwnerItemException.class);
    }

    @Test
    void getListYourBooking_allStatesReturnsCreatedBooking() {
        User owner = userRepository.save(User.builder()
                .name("owner")
                .email("owner@mail.com")
                .build());

        User booker = userRepository.save(User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build());

        Item item = itemService.addItem(ItemRegisterDto.builder()
                .name("Вещь")
                .description("описание")
                .available(true)
                .ownerId(owner.getId())
                .build());

        BookingRegisterDto dto = BookingRegisterDto.builder()
                .itemId(item.getId())
                .bookerId(booker.getId())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();

        bookingService.addBooking(dto);

        List<Booking> bookings = bookingService.getListYourBooking(booker.getId(), "ALL");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(booker.getId());
    }
}