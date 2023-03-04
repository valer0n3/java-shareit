package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestGetAllDto {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemForRequestDto> items;
}
