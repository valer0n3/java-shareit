package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, int userId);

    ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId);

    ItemDto getItemById(int itemId);

    List<ItemDto> getAllItemsForOwner(int userID);

    List<ItemDto> searchItem(String text);
}
