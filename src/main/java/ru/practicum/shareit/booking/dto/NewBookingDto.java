package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
public class NewBookingDto {
    @NotNull
    @Positive(message = "The value must be positive")
    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}

