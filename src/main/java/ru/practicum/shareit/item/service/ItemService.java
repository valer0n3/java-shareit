package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, int userId);

    ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId);

    ItemWithBookingDatesDto getItemById(int itemId, int userId);

    List<ItemWithBookingDatesDto> getAllItemsForOwner(int userID);

    List<ItemDto> searchItem(String text);

    CommentDto addComment(CommentDto commentDto, int itemId, int userId);
}
