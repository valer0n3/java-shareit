package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemStorageInMemoryImpl implements ItemStrorage {
    private Map<Integer, UserDto> itemHashMap = new HashMap<>();
    private static int id;

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

    private int idGenerator() {
        return ++id;
    }

    public boolean checkIfIdAlreadyExists(int id) {
        return itemHashMap.containsKey(id);
    }
}
