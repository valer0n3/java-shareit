package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorageInMemoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ItemStorageInMemoryImpl implements ItemStrorage {
    private final UserStorageInMemoryImpl userStorageInMemory;
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
        if (item.getName() != null && !item.getName().isBlank()) {
            itemHashMap.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemHashMap.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemHashMap.get(itemId).setAvailable(item.getAvailable());
        }
        return itemHashMap.get(itemId);
    }

    @Override
    public Item getItemById(int itemId) {
        return Optional.ofNullable(itemHashMap.get(itemId))
                .orElseThrow(() -> new IncorrectInputException(String.format("Id %d is not existed", itemId)));
    }

    @Override
    public List<Item> getAllItemsForOwner(int userID) {
        return itemHashMap.values().stream()
                .filter((item) -> item.getOwner() == userID)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemHashMap.values().stream()
                .filter(Item::getAvailable)
                .filter((item) -> item.getName().toLowerCase().contains(text.trim().toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    private int idGenerator() {
        return ++id;
    }

    public boolean checkIfUserIdAlreadyExists(int id) {
        return userStorageInMemory.getUserHashMap().containsKey(id);
    }

    @Override
    public boolean checkItemOwner(int userId, int itemID) {
        return itemHashMap.get(itemID).getOwner() == userId;
    }

    @Override
    public boolean checkIfItemIdExists(int itemID) {
        return itemHashMap.containsKey(itemID);
    }
}
