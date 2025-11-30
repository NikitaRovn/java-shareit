package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestRegisterDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestServiceIntegrationTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllRequests_returnsOnlyOthersRequests_sortedDesc() throws InterruptedException {
        User requester1 = userRepository.save(User.builder()
                .name("user1")
                .email("u1@mail.com")
                .build());
        User requester2 = userRepository.save(User.builder()
                .name("user2")
                .email("u2@mail.com")
                .build());

        Request r1 = requestService.registerRequest(
                RequestRegisterDto.builder().description("нужен молоток").build(),
                requester1.getId()
        );

        Thread.sleep(5);

        Request r2 = requestService.registerRequest(
                RequestRegisterDto.builder().description("нужна дрель").build(),
                requester2.getId()
        );

        List<RequestDto> result = requestService.getAllRequests(requester1.getId(), 0, 10);

        assertThat(result)
                .extracting(RequestDto::getId)
                .containsExactly(r2.getId());
    }
}