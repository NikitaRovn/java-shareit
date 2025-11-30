package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestRegisterDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static RequestDto mapFromRequestToRequestDto(Request request) {
        if (request == null) return null;

        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(
                        request.getItems() == null
                                ? List.of()
                                : request.getItems().stream()
                                .map(ItemMapper::mapFromItemToItemDto)
                                .toList()
                )
                .build();
    }

    public static List<RequestDto> mapFromListRequestToListRequestDto(List<Request> requests) {
        return requests.stream().map(RequestMapper::mapFromRequestToRequestDto).toList();
    }

    public static Request mapFromRequestRegisterDtoToRequest(RequestRegisterDto requestRegisterDto) {
        return Request.builder()
                .description(requestRegisterDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }
}
