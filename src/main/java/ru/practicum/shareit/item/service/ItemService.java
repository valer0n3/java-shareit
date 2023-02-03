package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDTO;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, int userId);

    ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId);

    ItemWithBookingDatesDTO getItemById(int itemId, int userId);

    List<ItemWithBookingDatesDTO> getAllItemsForOwner(int userID);

    List<ItemDto> searchItem(String text);

    CommentDto addComment(CommentDto commentDto, int itemId, int userId);
}
