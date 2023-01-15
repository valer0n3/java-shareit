package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private String available;
    private int owner;
    private ItemRequest request;
}
