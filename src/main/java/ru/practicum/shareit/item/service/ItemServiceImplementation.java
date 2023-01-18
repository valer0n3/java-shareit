package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStrorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStorage;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.mapItemDtoToItem(itemDto);
        return ItemMapper.mapItemToItemDto(itemStorage.addNewItem(item, userId));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        Item item = ItemMapper.mapItemDtoToItem(itemDto);
        return ItemMapper.mapItemToItemDto(itemStorage.updateItem(item, userId, itemId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.mapItemToItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userId) {
        return itemStorage.getAllItemsForOwner(userId).stream()
                .map(ItemMapper::mapItemToItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text).stream()
                .map((ItemMapper::mapItemToItemDto)).collect(Collectors.toList());
    }

    public void checkIfIdExists(int id) {
        if (!itemStorage.checkIfIdAlreadyExists(id)) {
            throw new IncorrectInputException(String.format("Id %i is not existed", id));
        }
    }
}
