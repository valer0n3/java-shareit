package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;

import java.util.List;

@Data
@Builder
public class ItemWithBookingDatesDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingOwnerDTO lastBooking;
    private BookingOwnerDTO nextBooking;
    private List<CommentDto> comments;
}
