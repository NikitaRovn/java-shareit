package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingUpdateDto {
    private Long id;

    private Long ownerId;

    private Boolean approved;
}
