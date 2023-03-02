package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingOwnerDTO {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
