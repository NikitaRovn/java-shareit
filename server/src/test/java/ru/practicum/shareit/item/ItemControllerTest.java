package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Test
    void addItem_validRequest_returnsOk() throws Exception {
        ItemRegisterDto registerDto = ItemRegisterDto.builder()
                .name("Дрель")
                .description("desc")
                .available(true)
                .build();

        ItemDto response = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("desc")
                .available(true)
                .ownerId(1L)
                .build();

        Mockito.when(itemService.addItem(any())).thenReturn(
                ru.practicum.shareit.item.model.Item.builder()
                        .id(1L)
                        .name("Дрель")
                        .description("desc")
                        .isAvailable(true)
                        .owner(ru.practicum.shareit.user.model.User.builder().id(1L).build())
                        .build()
        );

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Дрель")));
    }

    @Test
    void getYourItems_returnsList() throws Exception {
        Mockito.when(itemService.getYourItems(1L))
                .thenReturn(List.of(
                        ItemDto.builder().id(1L).name("i1").description("d1").available(true).ownerId(1L).build()
                ));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateItem_callsServiceWithPathVariables() throws Exception {
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .name("new")
                .build();

        Mockito.when(itemService.updateItem(eq(1L), any()))
                .thenReturn(
                        ru.practicum.shareit.item.model.Item.builder()
                                .id(1L)
                                .name("new")
                                .description("d")
                                .isAvailable(true)
                                .owner(ru.practicum.shareit.user.model.User.builder().id(1L).build())
                                .build()
                );

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("new")));
    }

    @Test
    void searchItems_returnsEmptyArrayWhenServiceReturnsEmpty() throws Exception {
        Mockito.when(itemService.searchItems("abc")).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}