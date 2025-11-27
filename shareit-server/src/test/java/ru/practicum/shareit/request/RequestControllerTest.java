package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestRegisterDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @Test
    void registerRequest_returnsOk() throws Exception {
        RequestRegisterDto reg = new RequestRegisterDto();
        reg.setDescription("нужна вещь");

        RequestDto dto = RequestDto.builder()
                .id(1L)
                .description("нужна вещь")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(requestService.registerRequest(reg, 1L))
                .thenReturn(Request.builder().id(1L).description("нужна вещь").created(dto.getCreated()).build());

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reg)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getMyRequests_returnsOk() throws Exception {
        RequestDto dto = RequestDto.builder()
                .id(1L)
                .description("req")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(requestService.getMyRequests(1L))
                .thenReturn(List.of(Request.builder().id(1L).description("req").created(dto.getCreated()).build()));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getAllRequests_returnsPagedList() throws Exception {
        Mockito.when(requestService.getAllRequests(1L, 0, 10))
                .thenReturn(List.of(
                        RequestDto.builder().id(1L).description("d1").build(),
                        RequestDto.builder().id(2L).description("d2").build()
                ));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getRequest_returnsOk() throws Exception {
        RequestDto dto = RequestDto.builder()
                .id(5L)
                .description("req")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(requestService.getRequest(5L, 1L))
                .thenReturn(Request.builder().id(5L).description("req").created(dto.getCreated()).build());

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }
}