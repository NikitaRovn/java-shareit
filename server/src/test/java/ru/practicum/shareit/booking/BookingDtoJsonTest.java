package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void serializeBookingDto() throws Exception {
        BookingDto dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, Month.JANUARY, 10, 12, 0))
                .end(LocalDateTime.of(2025, Month.JANUARY, 10, 13, 0))
                .item(ItemDto.builder()
                        .id(2L)
                        .name("Вещь")
                        .build())
                .booker(UserDto.builder()
                        .id(3L)
                        .name("Test User")
                        .email("test@mail.com")
                        .build())
                .status(StatusBooking.APPROVED)
                .build();

        var content = json.write(dto);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("Вещь");
        assertThat(content).extractingJsonPathStringValue("$.status")
                .isEqualTo("APPROVED");

        assertThat(content).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(3);
        assertThat(content).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo("Test User");
        assertThat(content).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("test@mail.com");
    }

    @Test
    void deserializeBookingDto() throws Exception {
        String body = "{"
                + "\"id\": 1,"
                + "\"start\": \"2025-01-10T12:00:00\","
                + "\"end\": \"2025-01-10T13:00:00\","
                + "\"item\": {\"id\": 2, \"name\": \"Вещь\"},"
                + "\"booker\": {\"id\": 3, \"name\": \"Test User\", \"email\": \"test@mail.com\"},"
                + "\"status\": \"WAITING\""
                + "}";

        BookingDto dto = json.parseObject(body);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart())
                .isEqualTo(LocalDateTime.of(2025, Month.JANUARY, 10, 12, 0));
        assertThat(dto.getEnd())
                .isEqualTo(LocalDateTime.of(2025, Month.JANUARY, 10, 13, 0));

        assertThat(dto.getItem().getId()).isEqualTo(2L);
        assertThat(dto.getItem().getName()).isEqualTo("Вещь");

        assertThat(dto.getBooker().getId()).isEqualTo(3L);
        assertThat(dto.getBooker().getName()).isEqualTo("Test User");
        assertThat(dto.getBooker().getEmail()).isEqualTo("test@mail.com");

        assertThat(dto.getStatus()).isEqualTo(StatusBooking.WAITING);
    }
}