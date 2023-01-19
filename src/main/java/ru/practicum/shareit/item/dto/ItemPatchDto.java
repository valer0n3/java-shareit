package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@Builder
public class ItemPatchDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}
