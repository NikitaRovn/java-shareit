package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRegisterDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void addBooking_valid_returnsOk() throws Exception {
        BookingRegisterDto dto = BookingRegisterDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();

        User owner = User.builder()
                .id(2L)
                .name("owner")
                .email("owner@mail.com")
                .build();

        User booker = User.builder()
                .id(1L)
                .name("booker")
                .email("booker@mail.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("Вещь")
                .description("описание")
                .isAvailable(true)
                .owner(owner)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(booker)
                .status(StatusBooking.WAITING)
                .build();

        Mockito.when(bookingService.addBooking(any())).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getBooking_returnsOk() throws Exception {
        User owner = User.builder()
                .id(2L)
                .name("owner")
                .email("owner@mail.com")
                .build();

        User booker = User.builder()
                .id(1L)
                .name("booker")
                .email("booker@mail.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("Вещь")
                .description("описание")
                .isAvailable(true)
                .owner(owner)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(booker)
                .status(StatusBooking.APPROVED)
                .build();

        Mockito.when(bookingService.getBooking(1L, 1L)).thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getListYourBooking_callsServiceWithState() throws Exception {
        Mockito.when(bookingService.getListYourBooking(1L, "ALL"))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getListYourItemsBooking_ownerEndpoint() throws Exception {
        Mockito.when(bookingService.getListYourItemsBooking(1L, "ALL"))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}