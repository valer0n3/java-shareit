package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Repository
public class ItemStorageInMemoryImpl implements ItemStrorage {
    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        return null;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        return null;
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userID) {
        return null;
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return null;
    }
}
