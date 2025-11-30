package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRegisterDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getYourItems_returnsOnlyOwnersItemsWithBookingsAndComments() {
        User owner = userRepository.save(User.builder()
                .name("owner")
                .email("owner@mail.com")
                .build());

        User other = userRepository.save(User.builder()
                .name("other")
                .email("other@mail.com")
                .build());

        ItemRegisterDto dto1 = ItemRegisterDto.builder()
                .name("Дрель")
                .description("хорошая дрель")
                .available(true)
                .ownerId(owner.getId())
                .build();
        ItemRegisterDto dto2 = ItemRegisterDto.builder()
                .name("Отвертка")
                .description("отвертка")
                .available(true)
                .ownerId(owner.getId())
                .build();
        ItemRegisterDto dto3 = ItemRegisterDto.builder()
                .name("Чужая вещь")
                .description("не твоя")
                .available(true)
                .ownerId(other.getId())
                .build();

        Item i1 = itemService.addItem(dto1);
        Item i2 = itemService.addItem(dto2);
        itemService.addItem(dto3);

        List<ItemDto> result = itemService.getYourItems(owner.getId());

        assertThat(result)
                .extracting(ItemDto::getId)
                .containsExactlyInAnyOrder(i1.getId(), i2.getId());
    }

    @Test
    void searchItems_blankQuery_returnsEmptyListWithoutDbSearch() {
        List<Item> result1 = itemService.searchItems("");
        List<Item> result2 = itemService.searchItems("   ");
        List<Item> result3 = itemService.searchItems(null);

        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        assertThat(result3).isEmpty();
    }
}