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
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDTO;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStrorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemStrorage itemStorage;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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
    public ItemWithBookingDatesDTO getItemById(int itemId, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", itemId)));
        BookingOwnerDTO lastBooking;
        BookingOwnerDTO nextBooking;
        if (userId == item.getOwner().getId()) {
            lastBooking = bookingMapper
                    .mapBookingToBookingOwnerDTO(bookingRepository.searchLatestBooking(itemId));
            nextBooking = bookingMapper
                    .mapBookingToBookingOwnerDTO(bookingRepository.searchNearestBooking(itemId));
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<Comment> comment1 = commentRepository.getCommentsOfItem(item.getId());
        List<CommentDto> comment = commentRepository.getCommentsOfItem(item.getId())
                .stream().map(commentMapper::mapCommentToCommentDto).collect(Collectors.toList());
        System.out.println("%%%%%%%%%%%%%% " + comment);
        return itemMapper
                .mapItemToItemWithBookingDatesDTO(item, lastBooking, nextBooking, comment);
    }

    @Override
    public List<ItemWithBookingDatesDTO> getAllItemsForOwner(int userId) {
        List<ItemWithBookingDatesDTO> ItemWithBookingDatesDTOList = new ArrayList<>();
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
        List<Item> items = itemRepository.getAllItemsForOwner(userId);
        BookingOwnerDTO lastBooking;
        BookingOwnerDTO nextBooking;
        for (Item item : items) {
            if (userId == item.getOwner().getId()) {
                lastBooking = bookingMapper
                        .mapBookingToBookingOwnerDTO(bookingRepository.searchLatestBooking(item.getId()));
                nextBooking = bookingMapper
                        .mapBookingToBookingOwnerDTO(bookingRepository.searchNearestBooking(item.getId()));
            } else {
                lastBooking = null;
                nextBooking = null;
            }
            List<CommentDto> comment = commentRepository.getCommentsOfItem(item.getId())
                    .stream().map(commentMapper::mapCommentToCommentDto).collect(Collectors.toList());
            ItemWithBookingDatesDTO itemWithBookingDatesDTO = itemMapper
                    .mapItemToItemWithBookingDatesDTO(item, lastBooking, nextBooking, comment);
            ItemWithBookingDatesDTOList.add(itemWithBookingDatesDTO);
        }
        return ItemWithBookingDatesDTOList;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", itemId)));
        bookingRepository.checkIfUserBookedItem(userId, itemId)
                .orElseThrow(() -> new IncorrectInputException(String
                        .format("User ID: %d did not book item ID: %d", userId, itemId)));
        Comment comment = commentMapper.mapCommentDtoToComment(commentDto);
        comment.setItem(item);
        comment.setCommentAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.mapCommentToCommentDto(commentRepository.save(comment));
    }
}
