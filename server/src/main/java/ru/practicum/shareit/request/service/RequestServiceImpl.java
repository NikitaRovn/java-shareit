package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestRegisterDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public Request registerRequest(RequestRegisterDto requestRegisterDto, Long userId) {
        Request request = RequestMapper.mapFromRequestRegisterDtoToRequest(requestRegisterDto);

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        request.setRequester(requester);

        return requestRepository.save(request);
    }

    @Override
    public List<Request> getMyRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return requestRepository.findByRequester_IdOrderByCreatedDesc(userId);
    }

    @Override
    public List<RequestDto> getAllRequests(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        Page<Request> page = requestRepository.findByRequester_IdNot(userId, pageable);

        return page.getContent()
                .stream()
                .map(RequestMapper::mapFromRequestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Request getRequest(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return requestRepository.findById(requestId)
                .orElseThrow(EntityNotFoundException::new);
    }
}
