package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("""
                SELECT i FROM Item i
                WHERE i.isAvailable = true
                  AND (
                        LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))
                     OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))
                  )
            """)
    List<Item> search(String query);

    List<Item> findByRequest_Id(Long requestId);
}
