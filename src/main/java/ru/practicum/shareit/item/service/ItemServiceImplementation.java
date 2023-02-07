package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        User user = getUserById(userId);
        Item item = itemMapper.mapItemDtoToItem(itemDto);
        item.setOwner(user);
        return itemMapper.mapItemToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId) {
        Item item = getItemById(itemId);
        checkIfItemOwnerEqualsUserId(item, userId);
        InsertInputDataToItem(item, itemPatchDto);
        return itemMapper.mapItemToItemPatchDto(itemRepository.save(item));
    }

    @Override
    public ItemWithBookingDatesDto getItemById(int itemId, int userId) {
        Item item = getItemById(itemId);
        BookingOwnerDTO lastBooking;
        BookingOwnerDTO nextBooking;
        if (checkIfItemOwnerEqualsUserId2(item, userId)) { //todo refactor this method completely.
            lastBooking = getLastBooking(item.getId());
            nextBooking = getNextBooking(item.getId());
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<CommentDto> comment = getListOfComments(item);
        return itemMapper
                .mapItemToItemWithBookingDatesDTO(item, lastBooking, nextBooking, comment);
    }

    @Override
    public List<ItemWithBookingDatesDto> getAllItemsForOwner(int userId) {
        List<ItemWithBookingDatesDto> itemWithBookingDatesDtoList = new ArrayList<>();
        getUserById(userId);
        List<Item> items = itemRepository.getAllItemsForOwner(userId);
        BookingOwnerDTO lastBooking;
        BookingOwnerDTO nextBooking;
        for (Item item : items) {
            if (checkIfItemOwnerEqualsUserId2(item, userId)) { //todo refactor this method completely.
                lastBooking = getLastBooking(item.getId());
                nextBooking = getNextBooking(item.getId());
            } else {
                lastBooking = null;
                nextBooking = null;
            }
            List<CommentDto> comment = getListOfComments(item);
            ItemWithBookingDatesDto itemWithBookingDatesDTO = itemMapper
                    .mapItemToItemWithBookingDatesDTO(item, lastBooking, nextBooking, comment);
            itemWithBookingDatesDtoList.add(itemWithBookingDatesDTO);
        }
        return itemWithBookingDatesDtoList;
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
                .map((itemMapper::mapItemToItemDto)).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, int userId) {
        User user = getUserById(userId);
        Item item = getItemById(itemId);
        bookingRepository.checkIfUserBookedItem(userId, itemId)
                .orElseThrow(() -> new IncorrectInputException(String
                        .format("User ID: %d did not book item ID: %d", userId, itemId)));
        Comment comment = commentMapper.mapCommentDtoToComment(commentDto);
        comment.setItem(item);
        comment.setCommentAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.mapCommentToCommentDto(commentRepository.save(comment));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
    }

    private Item getItemById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Item Id %d is not existed", itemId)));
    }

    private BookingOwnerDTO getLastBooking(int itemId) {
        return bookingMapper
                .mapBookingToBookingOwnerDTO(bookingRepository.searchLatestBooking(itemId));
    }

    private BookingOwnerDTO getNextBooking(int itemId) {
        return bookingMapper.mapBookingToBookingOwnerDTO(bookingRepository.searchNearestBooking(itemId));
    }

    private void checkIfItemOwnerEqualsUserId(Item item, int userId) {
        if (item.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Item does not belong to User and can not be updated!");
        }
    }

    private List<CommentDto> getListOfComments(Item item) {
        return commentRepository.getCommentsOfItem(item.getId())
                .stream()
                .map(commentMapper::mapCommentToCommentDto)
                .collect(Collectors.toList());
    }

    private boolean checkIfItemOwnerEqualsUserId2(Item item, int userId) {
        return item.getOwner().getId() == userId;
    }

    private void InsertInputDataToItem(Item item, ItemPatchDto itemPatchDto) {
        if (itemPatchDto.getName() != null && !itemPatchDto.getName().isBlank()) {
            item.setName(itemPatchDto.getName());
        }
        if (itemPatchDto.getDescription() != null && !itemPatchDto.getDescription().isBlank()) {
            item.setDescription(itemPatchDto.getDescription());
        }
        if (itemPatchDto.getAvailable() != null) {
            item.setAvailable(itemPatchDto.getAvailable());
        }
    }
}
