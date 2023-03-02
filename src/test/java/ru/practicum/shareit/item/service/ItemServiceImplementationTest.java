package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplementationTest {
    @InjectMocks
    private ItemServiceImplementation itemService;
    @Spy
    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    @Spy
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;
    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;
    private ItemDto itemDto;
    private ItemPatchDto itemPatchDto;
    private CommentDto commentDto;
    private Comment comment;
    private User user;
    private Request request;
    private Item item;
    private Booking bookingLast;
    private Booking bookingNext;

    @BeforeEach
    public void beforeEach() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("testItemDto")
                .description("testDescription")
                .available(true)
                .build();
        user = User.builder()
                .id(0)
                .name("testUser")
                .build();
        item = Item.builder()
                .id(1)
                .name("itemName")
                .description("itemDescr")
                .build();
        request = Request.builder()
                .id(1)
                .description("newRequest")
                .build();
        itemPatchDto = ItemPatchDto.builder()
                .id(1)
                .name("itemName")
                .build();
        bookingLast = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().minusDays(5))
                .status(BookingStatusEnum.APPROVED)
                .item(item)
                .build();
        bookingNext = Booking.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(4))
                .end(LocalDateTime.now().plusDays(10))
                .status(BookingStatusEnum.APPROVED)
                .item(item)
                .build();
        comment = Comment.builder()
                .id(1)
                .text("commentTest")
                .item(item)
                .build();
    }

    @Test
    public void addNewItem_whenUserAndRequestIsNotExisted_thenTrowObjectNotFoundException() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundExceptionUser =
                assertThrows(ObjectNotFoundException.class, () -> itemService.addNewItem(itemDto, userId));
        assertEquals(String.format("User Id %d is not existed", userId), objectNotFoundExceptionUser.getMessage());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        itemDto.setRequestId(1);
        when(requestRepository.findById(itemDto.getRequestId())).thenReturn(Optional.empty());
        IncorrectInputException incorrectInputExceptionRequest = assertThrows(IncorrectInputException.class, () -> itemService.addNewItem(itemDto, userId));
        assertEquals(String.format(String.format("Request with ID: %d", itemDto.getRequestId())), incorrectInputExceptionRequest.getMessage());
    }

    @Test
    public void addNewItem_whenRequestEquals0_thenReturnItemWithRequestNull() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        itemDto.setRequestId(0);
        itemService.addNewItem(itemDto, userId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(itemDto.getName(), itemArgumentCaptor.getValue().getName());
        assertNotNull(itemArgumentCaptor.getValue().getOwner());
        assertEquals(user.getName(), itemArgumentCaptor.getValue().getOwner().getName());
        assertNull(itemArgumentCaptor.getValue().getRequest());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void addNewItem_whenRequestIsnNot0_thenReturnItemWithRequestNull() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        itemDto.setRequestId(1);
        doReturn(Optional.of(request)).when(requestRepository).findById(itemDto.getRequestId());
        itemService.addNewItem(itemDto, userId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(itemDto.getName(), itemArgumentCaptor.getValue().getName());
        assertNotNull(itemArgumentCaptor.getValue().getRequest());
        assertEquals(user.getName(), itemArgumentCaptor.getValue().getOwner().getName());
        assertNotNull(itemArgumentCaptor.getValue().getRequest());
    }

    @Test
    public void updateItem_whenItemIsNotExisted_thenThrowObjectNotFoundException() {
        int userId = 0;
        int itemId = 0;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> itemService.updateItem(itemPatchDto, userId, itemId));
        assertEquals(String.format("Item Id %d is not existed", itemId), objectNotFoundException.getMessage());
    }

    @Test
    public void updateItem_whenItemOwnerIsNotEqualUserId_thenThrowObjectNotFoundException() {
        int userId = 0;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setOwner(user);
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> itemService.updateItem(itemPatchDto, userId, itemId));
        assertEquals("Item does not belong to User and can not be updated!",
                objectNotFoundException.getMessage());
    }

    @Test
    public void updateItem_whenItemOwnerEqualsToUserIdAndNameIsNotNull_thenSaveName() {
        int userId = 1;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setOwner(user);
        item.setDescription(null);
        itemPatchDto.setName("itemName");
        itemPatchDto.setDescription(null);
        itemPatchDto.setAvailable(null);
        itemService.updateItem(itemPatchDto, userId, itemId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(itemPatchDto.getName(), itemArgumentCaptor.getValue().getName());
        assertNull(itemArgumentCaptor.getValue().getDescription());
        assertNull(itemArgumentCaptor.getValue().getAvailable());
    }

    @Test
    public void updateItem_whenDescriptionIsNotNullAndNotBlank_thenSaveDescription() {
        int userId = 1;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setDescription(null);
        item.setName(null);
        item.setAvailable(null);
        item.setOwner(user);
        itemPatchDto.setDescription("itemDescription");
        itemPatchDto.setName(null);
        itemPatchDto.setAvailable(null);
        itemService.updateItem(itemPatchDto, userId, itemId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(itemPatchDto.getDescription(), itemArgumentCaptor.getValue().getDescription());
        assertNull(itemArgumentCaptor.getValue().getName());
        assertNull(itemArgumentCaptor.getValue().getAvailable());
    }

    @Test
    public void updateItem_whenAvailableIsNotNullAndNotBlank_thenSaveSescription() {
        int userId = 1;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setName(null);
        item.setDescription(null);
        item.setOwner(user);
        itemPatchDto.setDescription(null);
        itemPatchDto.setName(null);
        itemPatchDto.setAvailable(true);
        itemService.updateItem(itemPatchDto, userId, itemId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertTrue(itemArgumentCaptor.getValue().getAvailable());
        assertNull(itemArgumentCaptor.getValue().getName());
        assertNull(itemArgumentCaptor.getValue().getDescription());
    }

    @Test
    public void updateItem_whenNameIsBlank_thenNotChangeNameWhenSave() {
        int userId = 1;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setDescription(null);
        item.setOwner(user);
        itemPatchDto.setName("   ");
        itemPatchDto.setDescription(null);
        itemPatchDto.setAvailable(null);
        itemService.updateItem(itemPatchDto, userId, itemId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(item.getName(), itemArgumentCaptor.getValue().getName());
        assertNull(itemArgumentCaptor.getValue().getDescription());
        assertNull(itemArgumentCaptor.getValue().getAvailable());
    }

    @Test
    public void updateItem_whenDescriptionIsBlank_thenNotChangeDescriptionwhenSave() {
        int userId = 1;
        int itemOwnerId = 1;
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        user.setId(itemOwnerId);
        item.setName(null);
        item.setAvailable(null);
        item.setOwner(user);
        itemPatchDto.setDescription("   ");
        itemPatchDto.setName(null);
        itemPatchDto.setAvailable(null);
        itemService.updateItem(itemPatchDto, userId, itemId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(item.getDescription(), itemArgumentCaptor.getValue().getDescription());
        assertNull(itemArgumentCaptor.getValue().getName());
        assertNull(itemArgumentCaptor.getValue().getAvailable());
    }

    @Test
    public void getItemById_whenItemOwnerEqualsUserID_thenGetItemWithBookingDatesDto() {
        int itemId = 1;
        int userId = 1;
        user.setId(1);
        item.setOwner(user);
        item.setDescription("itemDescription");
        item.setAvailable(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.getBookingForItem(anyInt())).thenReturn(List.of(bookingLast, bookingNext));
        when(commentRepository.getCommentForItem(anyInt())).thenReturn(List.of(comment));
        ItemWithBookingDatesDto itemWithBookingDatesDto = itemService.getItemById(itemId, userId);
        assertEquals(item.getName(), itemWithBookingDatesDto.getName());
        List<CommentDto> expectedCommentList = List.of(commentMapper.mapCommentToCommentDto(comment));
        assertEquals(expectedCommentList, itemWithBookingDatesDto.getComments());
        assertEquals(bookingLast.getStart(), itemWithBookingDatesDto.getLastBooking().getStart());
        assertEquals(bookingLast.getEnd(), itemWithBookingDatesDto.getLastBooking().getEnd());
        assertEquals(bookingNext.getStart(), itemWithBookingDatesDto.getNextBooking().getStart());
        assertEquals(bookingNext.getEnd(), itemWithBookingDatesDto.getNextBooking().getEnd());
    }

    @Test
    public void getItemById_whenItemOwnerNotEqualsUserID_thenLastNextDatesAreNull() {
        int itemId = 1;
        int userId = 5;
        user.setId(1);
        item.setOwner(user);
        item.setDescription("itemDescription");
        item.setAvailable(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.getBookingForItem(anyInt())).thenReturn(List.of(bookingLast, bookingNext));
        when(commentRepository.getCommentForItem(anyInt())).thenReturn(List.of(comment));
        ItemWithBookingDatesDto itemWithBookingDatesDto = itemService.getItemById(itemId, userId);
        assertEquals(item.getName(), itemWithBookingDatesDto.getName());
        List<CommentDto> expectedCommentList = List.of(commentMapper.mapCommentToCommentDto(comment));
        assertEquals(expectedCommentList, itemWithBookingDatesDto.getComments());
        assertNull(itemWithBookingDatesDto.getLastBooking());
        assertNull(itemWithBookingDatesDto.getNextBooking());
    }

    @Test
    public void getAllItemsForOwner_whenInputIsCorrect_thenReturnItemWithBookingDatesDto() {
        int userId = 1;
        user.setId(1);
        item.setOwner(user);
        item.setDescription("itemDescription");
        item.setAvailable(true);
        when(itemRepository.getAllItemsForOwner(userId)).thenReturn(List.of(item));
        when(bookingRepository.getBookingForOwner(userId)).thenReturn(List.of(bookingLast, bookingNext));
        when(commentRepository.getCommentForOwner(any())).thenReturn(List.of(comment));
        List<ItemWithBookingDatesDto> itemWithBookingDatesDtos = itemService.getAllItemsForOwner(userId);
        assertEquals(item.getName(), itemWithBookingDatesDtos.get(0).getName());
        List<CommentDto> expectedCommentList = List.of(commentMapper.mapCommentToCommentDto(comment));
        assertEquals(expectedCommentList, itemWithBookingDatesDtos.get(0).getComments());
        assertEquals(bookingLast.getStart(), itemWithBookingDatesDtos.get(0).getLastBooking().getStart());
        assertEquals(bookingLast.getEnd(), itemWithBookingDatesDtos.get(0).getLastBooking().getEnd());
        assertEquals(bookingNext.getStart(), itemWithBookingDatesDtos.get(0).getNextBooking().getStart());
        assertEquals(bookingNext.getEnd(), itemWithBookingDatesDtos.get(0).getNextBooking().getEnd());
    }

    @Test
    public void searchItem_whenTestIsNotBlank_thenReturnSearchResults() {
        String searchInput = "itemName";
        when(itemRepository.searchItem(searchInput)).thenReturn(List.of(item));
        itemService.searchItem(searchInput);
        verify(itemRepository).searchItem(searchInput);
    }

    @Test
    public void searchItem_whenTestIsBlank_thenReturnEmptyArrayList() {
        String emptySearchInput = " ";
        List<ItemDto> itemDtoList = itemService.searchItem(emptySearchInput);
        verify(itemRepository, times(0)).searchItem(emptySearchInput);
        assertEquals(0, itemDtoList.size());
    }

    @Test
    public void addComment_whenInputIsCorrect_thenSaveComment() {
        int itemId = 0;
        int userId = 0;
        commentDto = CommentDto.builder()
                .text("newCommentText")
                .id(1)
                .created(LocalDateTime.now())
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.checkIfUserBookedItem(userId, itemId)).thenReturn(Optional.of(bookingLast));
        CommentDto resultCommentDto = itemService.addComment(commentDto, itemId, userId);
        verify(commentRepository, times(1)).save(commentArgumentCaptor.capture());
        assertNotNull(commentArgumentCaptor.getValue().getCommentAuthor());
        assertNotNull(commentArgumentCaptor.getValue().getCreated());
    }

    @Test
    public void addComment_whencheckIfUserBookedItemNotExists_thenThrowIncorrectInputException() {
        int itemId = 0;
        int userId = 0;
        commentDto = CommentDto.builder()
                .text("newCommentText")
                .id(1)
                .created(LocalDateTime.now())
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.checkIfUserBookedItem(userId, itemId)).thenReturn(Optional.empty());
        assertThrows(IncorrectInputException.class,
                () -> itemService.addComment(commentDto, itemId, userId));
    }
}