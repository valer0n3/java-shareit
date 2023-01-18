package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemPatchDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
   // private int owner;
    private ItemRequest request;
}
