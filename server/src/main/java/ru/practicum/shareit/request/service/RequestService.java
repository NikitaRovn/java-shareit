package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestRegisterDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request registerRequest(RequestRegisterDto requestRegisterDto, Long userId);

    List<Request> getMyRequests(Long userId);

    List<RequestDto> getAllRequests(Long userId, int from, int size);

    Request getRequest(Long requestId, Long userId);
}
