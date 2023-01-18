package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int userId, int itemId);

    ItemDto getItemById(int itemId);

    List<ItemDto> getAllItemsForOwner(int userID);

    List<ItemDto> searchItem(String text);
}