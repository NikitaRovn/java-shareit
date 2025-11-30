package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void serializeRequestDto_withItems() throws Exception {
        RequestDto dto = RequestDto.builder()
                .id(1L)
                .description("нужна дрель")
                .created(LocalDateTime.of(2025, Month.JANUARY, 5, 10, 0))
                .items(List.of(
                        ItemDto.builder().id(2L).name("Дрель").description("описание").available(true).ownerId(3L).build()
                ))
                .build();

        var content = json.write(dto);

        assertThat(content).extractingJsonPathStringValue("$.description").isEqualTo("нужна дрель");
        assertThat(content).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(content).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(2);
    }
}