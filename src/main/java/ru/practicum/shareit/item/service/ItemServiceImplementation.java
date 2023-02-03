package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDTO;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStrorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStorage;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
        Item item = itemMapper.mapItemDtoToItem(itemDto);
        item.setOwner(user);
        return itemMapper.mapItemToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Item Id %d is not existed", itemId)));
        if (item.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Item does not belong to User and can not be updated!");
        }
        if (itemPatchDto.getName() != null && !itemPatchDto.getName().isBlank()) {
            item.setName(itemPatchDto.getName());
        }
        if (itemPatchDto.getDescription() != null && !itemPatchDto.getDescription().isBlank()) {
            item.setDescription(itemPatchDto.getDescription());
        }
        if (itemPatchDto.getAvailable() != null) {
            item.setAvailable(itemPatchDto.getAvailable());
        }
        return itemMapper.mapItemToItemPatchDto(itemRepository.save(item));
    }

    @Override
    public ItemWithBookingDatesDTO getItemById(int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", itemId)));
        BookingOwnerDTO lastBooking = bookingMapper
                .mapBookingToBookingOwnerDTO(bookingRepository.searchLatestBooking(itemId));
        BookingOwnerDTO nextBooking = bookingMapper
                .mapBookingToBookingOwnerDTO(bookingRepository.searchNearestBooking(itemId));
        return itemMapper
                .mapItemToItemWithBookingDatesDTO(item, lastBooking, nextBooking);
    }

    @Override
    public List<ItemDto> getAllItemsForOwner(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
        List<Item> items = itemRepository.getAllItemsForOwner(userId);
        return itemRepository.getAllItemsForOwner(userId).stream()
                .map((itemMapper::mapItemToItemDto)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
                .map((itemMapper::mapItemToItemDto)).collect(Collectors.toList());
    }
}
