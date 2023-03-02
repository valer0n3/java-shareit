package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.Request;

@Data
@Builder
public class ItemPatchDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Request request;
}
