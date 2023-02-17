package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplementationTest {
    @InjectMocks
    private ItemServiceImplementation itemService;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentMapper commentMapper;
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
    private ItemDto itemDto;
    private ItemPatchDto itemPatchDto;
    private ItemWithBookingDatesDto itemWithBookingDatesDto;
    private CommentDto commentDto;
    private User user;
    private Request request;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void beforeEachCreateRequests() {
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
    }

    @Test
    void addNewItem_whenUserAndRequestIsNotExisted_thenTrowObjectNotFoundException() {
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
    void addNewItem_whenRequestEquals0_thenReturnItemWithRequestNull() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        itemDto.setRequestId(0);
        when(itemMapper.mapItemDtoToItem(any())).thenReturn(item);
        itemService.addNewItem(itemDto, userId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(item.getName(), itemArgumentCaptor.getValue().getName());
        assertNotNull(itemArgumentCaptor.getValue().getRequest());
        assertEquals(user.getName(), itemArgumentCaptor.getValue().getOwner().getName());
        assertNull(itemArgumentCaptor.getValue().getRequest());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void addNewItem_whenRequestIsnNot0_thenReturnItemWithRequestNull() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        itemDto.setRequestId(1);
        doReturn(Optional.of(request)).when(requestRepository).findById(itemDto.getRequestId());
        when(itemMapper.mapItemDtoToItem(any())).thenReturn(item);
        itemService.addNewItem(itemDto, userId);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(item.getName(), itemArgumentCaptor.getValue().getName());
        assertNotNull(itemArgumentCaptor.getValue().getRequest());
        assertEquals(user.getName(), itemArgumentCaptor.getValue().getOwner().getName());
        assertNotNull(itemArgumentCaptor.getValue().getRequest());
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void getAllItemsForOwner() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void addComment() {
    }

    @Test
    void addBookingAndComments() {
    }
}