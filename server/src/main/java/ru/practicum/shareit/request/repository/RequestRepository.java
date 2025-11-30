package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester_IdOrderByCreatedDesc(Long requesterId);

    Optional<Request> findById(Long id);

    Page<Request> findByRequester_IdNot(Long userId, Pageable pageable);

    List<Request> findByRequester_IdNotOrderByCreatedDesc(Long requesterId);
}
