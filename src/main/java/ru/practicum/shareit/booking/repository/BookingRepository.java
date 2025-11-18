package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(Long bookerId,
                                                           LocalDateTime start,
                                                           LocalDateTime end,
                                                           Sort sort);

    List<Booking> findByBooker_IdAndEndBefore(Long bookerId,
                                              LocalDateTime end,
                                              Sort sort);

    List<Booking> findByBooker_IdAndStartAfter(Long bookerId,
                                               LocalDateTime start,
                                               Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId,
                                           StatusBooking status,
                                           Sort sort);

    List<Booking> findByItem_Owner_Id(Long ownerId, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(Long ownerId,
                                                               LocalDateTime start,
                                                               LocalDateTime end,
                                                               Sort sort);

    List<Booking> findByItem_Owner_IdAndEndBefore(Long ownerId,
                                                  LocalDateTime end,
                                                  Sort sort);

    List<Booking> findByItem_Owner_IdAndStartAfter(Long ownerId,
                                                   LocalDateTime start,
                                                   Sort sort);

    List<Booking> findByItem_Owner_IdAndStatus(Long ownerId,
                                               StatusBooking status,
                                               Sort sort);

    boolean existsByItemAndBooker(Item item, User booker);

    Optional<Booking> findByItem_IdAndBooker_Id(Long itemId, Long bookerId);

    Optional<Booking> findByItemAndBookerAndEndBefore (Item item, User user, LocalDateTime now);

    boolean existsByItemAndBookerAndStatusAndEndBefore(Item item, User user, StatusBooking statusBooking, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

}
