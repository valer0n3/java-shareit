package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplementationTest {
    @InjectMocks
    private BookingServiceImplementation bookingService;
    @Spy
    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    @Spy
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;
    private User user;
    private Item item;
    private NewBookingDto newBookingDto;
    private Booking returnedBooking;

    @BeforeEach
    public void beforeEachCreateRequests() {
        user = User.builder()
                .id(1)
                .name("testUser")
                .email("testEmail")
                .build();
        newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        item = Item.builder()
                .id(1)
                .name("testItem")
                .description("testDescription")
                .owner(user)
                .available(true)
                .build();
        returnedBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .item(item)
                .booker(user)
                .status(BookingStatusEnum.WAITING)
                .build();
    }

    @Test
    void addNewBooking_whenUserIsNotExisted_thenThrowObjectNotFoundException() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> bookingService.addNewBooking(newBookingDto, userId));
    }

    @Test
    void addNewBooking_whenItemIsNotExisted_thenThrowObjectNotFoundException() {
        int userId = 1;
        int itemId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> bookingService.addNewBooking(newBookingDto, userId));
    }

    @Test
    void addNewBooking_whenUserIsNotItemOwner_thenThrowObjectNotFoundException() {
        int userId = 1;
        int itemId = 1;
        item.setOwner(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(ObjectNotFoundException.class, () -> bookingService.addNewBooking(newBookingDto, userId));
    }

    @Test
    void addNewBooking_whenItemIsUnavailable_thenThrowIncorrectInputException() {
        int userId = 5;
        int itemId = 1;
        item.setOwner(user);
        item.setAvailable(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(IncorrectInputException.class, () -> bookingService.addNewBooking(newBookingDto, userId));
    }

    @Test
    void addNewBooking_whenBookingEndDateIsAfterStartDate_thenThrowIncorrectInputException() {
        int userId = 5;
        int itemId = 1;
        item.setOwner(user);
        item.setAvailable(true);
        newBookingDto.setStart(LocalDateTime.now().plusDays(5));
        newBookingDto.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(IncorrectInputException.class, () -> bookingService.addNewBooking(newBookingDto, userId));
    }

    @Test
    void addNewBooking_whenAllInputIsCorrect_thenSave() {
        int userId = 5;
        int itemId = 1;
        item.setOwner(user);
        item.setAvailable(true);
        newBookingDto.setStart(LocalDateTime.now().plusDays(1));
        newBookingDto.setEnd(LocalDateTime.now().plusDays(5));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        BookingDto bookingDtoReturned = bookingService.addNewBooking(newBookingDto, userId);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        assertNotNull(bookingArgumentCaptor.getValue().getBooker());
        assertNotNull(bookingArgumentCaptor.getValue().getItem());
        assertEquals(BookingStatusEnum.WAITING, bookingArgumentCaptor.getValue().getStatus());
    }

    @Test
    void confirmBookingRequest_whenUserIsNotItemOwner_thenObjectNotFoundException() {
        int userId = 5;
        int bookingId = 1;
        boolean isApproved = true;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        assertThrows(ObjectNotFoundException
                        .class,
                () -> bookingService.confirmBookingRequest(userId, bookingId, isApproved));
    }

    @Test
    void confirmBookingRequest_whenBookingStatusIsNotEqualsToWaiting_thenIncorrectInputException() {
        int userId = 1;
        int bookingId = 1;
        boolean isApproved = true;
        returnedBooking.setStatus(BookingStatusEnum.APPROVED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        assertThrows(IncorrectInputException
                        .class,
                () -> bookingService.confirmBookingRequest(userId, bookingId, isApproved));
    }

    @Test
    void confirmBookingRequest_whenBookingStatusIsApproved_thenSaveApproved() {
        int userId = 1;
        int bookingId = 1;
        boolean isApproved = true;
        returnedBooking.setStatus(BookingStatusEnum.WAITING);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        BookingDto bookingDtoReturned = bookingService.confirmBookingRequest(userId, bookingId, isApproved);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        assertEquals(BookingStatusEnum.APPROVED, bookingArgumentCaptor.getValue().getStatus());
    }

    @Test
    void confirmBookingRequest_whenBookingStatusIsRejected_thenSaveApproved() {
        int userId = 1;
        int bookingId = 1;
        boolean isApproved = false;
        returnedBooking.setStatus(BookingStatusEnum.WAITING);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        BookingDto bookingDtoReturned = bookingService.confirmBookingRequest(userId, bookingId, isApproved);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        assertEquals(BookingStatusEnum.REJECTED, bookingArgumentCaptor.getValue().getStatus());
    }

    @Test
    void getBookingById_whenBookerIdIsNotEqualsToUserId_thenTrowObjectNotFoundException() {
        int bookingId = 1;
        int userId = 5;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getBookingById_whenItemOwnerIdIsNotEqualsToUserId_thenTrowObjectNotFoundException() {
        int bookingId = 1;
        int userId = 1;
        User booker = User.builder()
                .id(1)
                .name("bookerName")
                .email("booker@email.com")
                .build();
        returnedBooking.getItem().getOwner().setId(5);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getBookingById_whenInputIsCorrect_thenReturnBookingDto() {
        int bookingId = 1;
        int userId = 1;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(returnedBooking));
        bookingService.getBookingById(bookingId, userId);
        verify(bookingRepository).save(returnedBooking);
    }

    @Test
    void getAllBookingsOfCurrentUser() {
    }

    @Test
    void getAllBookingsOfAllUserItems() {
    }

    @Test
    void checkIfItemIsAvailable() {
    }
}