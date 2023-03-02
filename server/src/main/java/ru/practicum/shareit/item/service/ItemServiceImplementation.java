package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
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
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final RequestRepository requestRepository;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, int userId) {
        User user = getUserById(userId);
        Request request = checkIfRequestIdExists(itemDto);
        Item item = itemMapper.mapItemDtoToItem(itemDto);
        item.setOwner(user);
        item.setRequest(request);
        return itemMapper.mapItemToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemPatchDto, int userId, int itemId) {
        Item item = getItemById(itemId);
        checkIfItemOwnerEqualsUserId(item, userId);
        insertInputDataToItem(item, itemPatchDto);
        return itemMapper.mapItemToItemPatchDto(itemRepository.save(item));
    }

    @Override
    public ItemWithBookingDatesDto getItemById(int itemId, int userId) {
        Item item = getItemById(itemId);
        List<Booking> booking = bookingRepository.getBookingForItem(itemId);
        List<Comment> comment = commentRepository.getCommentForItem(itemId);
        return addBookingAndComments(item, booking, comment, userId);
    }

    @Override
    public List<ItemWithBookingDatesDto> getAllItemsForOwner(int userId) {
        List<Item> items = itemRepository.getAllItemsForOwner(userId);
        List<Booking> booking = bookingRepository.getBookingForOwner(userId);
        List<Integer> itemIdList = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Comment> comment = commentRepository.getCommentForOwner(itemIdList);
        return items.stream()
                .map(item -> addBookingAndComments(item, booking, comment, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
                .map((itemMapper::mapItemToItemDto))
                .collect(Collectors.toList());
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

    private void checkIfItemOwnerEqualsUserId(Item item, int userId) {
        if (item.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Item does not belong to User and can not be updated!");
        }
    }

    private void insertInputDataToItem(Item item, ItemPatchDto itemPatchDto) {
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

    public ItemWithBookingDatesDto addBookingAndComments(Item item, List<Booking> booking, List<Comment> comment, int userId) {
        ItemDto itemDto = itemMapper.mapItemToItemDto(item);
        Optional<Booking> lastBookingDate;
        Optional<Booking> nextBookingDate;
        if (item.getOwner().getId() != userId) {
            lastBookingDate = Optional.empty();
            nextBookingDate = Optional.empty();
        } else {
            lastBookingDate = booking.stream()
                    .filter(bookingField -> bookingField.getItem().getId() == item.getId())
                    .filter(bookingField -> bookingField.getStatus().equals(BookingStatusEnum.APPROVED))
                    .filter(bookingField -> bookingField.getEnd().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(Booking::getEnd));
            nextBookingDate = booking.stream()
                    .filter(bookingField -> bookingField.getItem().getId() == item.getId())
                    .filter(bookingField -> bookingField.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(Booking::getStart));
        }
        List<CommentDto> itemComments = comment.stream()
                .filter(comment1 -> comment1.getItem().getId() == (item.getId()))
                .map(commentMapper::mapCommentToCommentDto)
                .collect(Collectors.toList());
        BookingOwnerDTO lastBookingDateMapped = bookingMapper.mapBookingToBookingOwnerDTO(lastBookingDate.orElse(null));
        BookingOwnerDTO nextBookingDateMapped = bookingMapper.mapBookingToBookingOwnerDTO(nextBookingDate.orElse(null));
        return itemMapper
                .mapItemToItemWithBookingDatesDTO(itemDto, lastBookingDateMapped, nextBookingDateMapped, itemComments);
    }

    private Request checkIfRequestIdExists(ItemDto itemDto) {
        if (itemDto.getRequestId() != 0) {
            return requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new IncorrectInputException(String.format("Request with ID: %d", itemDto.getRequestId())));
        }
        return null;
    }
}
