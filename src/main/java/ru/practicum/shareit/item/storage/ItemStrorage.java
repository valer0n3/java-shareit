package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStrorage {
    Item addNewItem(Item item, int userId);

    Item updateItem(Item item, int userId, int itemId);

    Item getItemById(int itemId);

    List<Item> getAllItemsForOwner(int userID);

    List<Item> searchItem(String text);

    boolean checkIfIdAlreadyExists(int id);

    boolean checkItemOwner(int userId, int itemID);

    boolean checkIfItemIdExists(int itemID);
}
