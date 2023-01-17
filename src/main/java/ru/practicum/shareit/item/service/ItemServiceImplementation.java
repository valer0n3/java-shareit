package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStrorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStorage;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        return itemStorage.addNewItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        return itemStorage.updateItem(itemDto, userId, itemId);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userId) {
        return itemStorage.getAllItemsForOwner(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text);
    }

    public void checkIfIdExists(int id) {
        if (!itemStorage.checkIfIdAlreadyExists(id)) {
            throw new IncorrectInputException(String.format("Id %i is not existed", id));
        }
    }
}
