package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStrorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStrorage;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        return itemStrorage.addNewItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        return itemStrorage.updateItem(itemDto, userId, itemId);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return itemStrorage.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userId) {
        return itemStrorage.getAllItemsForOwner(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStrorage.searchItem(text);
    }
}
