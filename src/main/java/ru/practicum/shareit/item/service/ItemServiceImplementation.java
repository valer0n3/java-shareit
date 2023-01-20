package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStrorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStorage;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        checkIfUserIdExists(userId);
        Item item = itemMapper.mapItemDtoToItem(itemDto);
        return itemMapper.mapItemToItemDto(itemStorage.addNewItem(item, userId));
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId) {
        Item item = itemMapper.mapItemPatchDtoToItem(itemPatchDto);
        checkItemOwner(userId, itemId);
        return itemMapper.mapItemToItemPatchDto(itemStorage.updateItem(item, userId, itemId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        checkIfItemIdExists(itemId);
        return itemMapper.mapItemToItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userId) {
        return itemStorage.getAllItemsForOwner(userId).stream()
                .map(itemMapper::mapItemToItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(text).stream()
                .map((itemMapper::mapItemToItemDto)).collect(Collectors.toList());
    }

    private void checkIfUserIdExists(int id) {
        if (!itemStorage.checkIfUserIdAlreadyExists(id)) {
            throw new ObjectNotFoundException(String.format("User with id: %d is not existed", id));
        }
    }

    private void checkItemOwner(int userId, int itemId) {
        if (!itemStorage.checkItemOwner(userId, itemId)) {
            throw new ObjectNotFoundException("Item does not belong to User and can not be updated!");
        }
    }

    private void checkIfItemIdExists(int itemId) {
        if (!itemStorage.checkIfItemIdExists(itemId)) {
            throw new ObjectNotFoundException(String.format("Item with ID %d was not found", itemId));
        }
    }
}
