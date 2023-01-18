package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemStorageInMemoryImpl implements ItemStrorage {
    private Map<Integer, Item> itemHashMap = new HashMap<>();
    private static int id;

    @Override
    public Item addNewItem(Item item, int userId) {
        int itemID = idGenerator();
        item.setOwner(userId);
        item.setId(itemID);
        itemHashMap.put(itemID, item);
        return itemHashMap.get(itemID);
    }

    @Override
    public Item updateItem(Item item, int userId, int itemId) {
        if (!item.getName().isBlank()) {
            itemHashMap.get(itemId).setName(item.getName());
        }
        if (!item.getDescription().isBlank()) {
            itemHashMap.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemHashMap.get(itemId).setAvailable(item.getAvailable());
        }
        return null;
    }

    @Override
    public Item getItemById(int itemId) {
        return null;
    }

    @Override
    public List<Item> getAllItemsForOwner(int userID) {
        return null;
    }

    @Override
    public List<Item> searchItem(String text) {
        return null;
    }

    private int idGenerator() {
        return ++id;
    }

    public boolean checkIfIdAlreadyExists(int id) {
        return itemHashMap.containsKey(id);
    }

    @Override
    public boolean checkItemOwner(int userId, int itemID) {
        return itemHashMap.get(itemID).getOwner() == userId;
    }
}
