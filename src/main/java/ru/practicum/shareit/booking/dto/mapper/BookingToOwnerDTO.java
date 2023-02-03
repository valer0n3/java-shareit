package ru.practicum.shareit.booking.dto.mapper;

import lombok.Builder;
import lombok.Data;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingToOwnerDTO {
    private int id;
    private int bookerid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
