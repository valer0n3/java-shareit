package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class BookingServiceImplementation implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addNewBooking(NewBookingDto newBookingDto, int userId) {
        User booker = getUserById(userId);
        Item item = getItemById(newBookingDto.getItemId());
        checkIfUserIsItemOwner(userId, item.getOwner().getId());
        checkIfItemIsAvailable(item);
        checkIfBookingEndDateIsAfterBookingStartDate(newBookingDto);
        Booking booking = bookingMapper.mapNewBookingDtoToBooking(newBookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatusEnum.WAITING);
        return getBookingDto(booking);
    }

    @Override
    public BookingDto confirmBookingRequest(int userId, int bookingId, boolean isApproved) {
        getUserById(userId);
        Booking booking = getBookingById(bookingId);
        checkIfUserIsNotItemOwner(userId, booking.getItem().getOwner().getId());
        checkIfBookingStatusIsWaiting(booking);
        booking.setStatus(approveBookingOrReject(isApproved));
        return getBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        Booking booking = getBookingById(bookingId);
        checkIfUserCanAccessToBooking(booking, userId);
        return getBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsOfCurrentUser(int userId, BookingStatusEnum state, int from, int size) {
        getUserById(userId);
        if (state.equals(BookingStatusEnum.ALL)) {
            Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("start").descending());
            Page<Booking> bookings = bookingRepository.findByBookerId(userId, pageWithElements);
            return getPageBookingDTO(bookings);
        } else if (state.equals(BookingStatusEnum.CURRENT)) {
            return getListBookingDTO(bookingRepository.getCurrentBookingsOfCurrentUser(userId));
        } else if (state.equals(BookingStatusEnum.PAST)) {
            return getListBookingDTO(bookingRepository.getPastBookingsOfCurrentUser(userId));
        } else if (state.equals(BookingStatusEnum.FUTURE)) {
            return getListBookingDTO(bookingRepository.getFutureBookingsOfCurrentUser(userId));
        } else if (state.equals(BookingStatusEnum.WAITING)) {
            return getListBookingDTO(bookingRepository.getBookingsOfCurrentUser(userId, BookingStatusEnum.WAITING.name()));
        } else if (state.equals(BookingStatusEnum.REJECTED)) {
            return getListBookingDTO(bookingRepository.getBookingsOfCurrentUser(userId, BookingStatusEnum.REJECTED.name()));
        } else
            throw new UnsupportedStatus(String.format("Unknown state: %s", state));
    }

    @Override
    public List<BookingDto> getAllBookingsOfAllUserItems(int userId, BookingStatusEnum state, int from, int size) {
        getUserById(userId);
        if (state.equals(BookingStatusEnum.ALL)) {
            Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("start").descending());
            return getPageBookingDTO(bookingRepository.findByItemOwnerId(userId, pageWithElements));
        } else if (state.equals(BookingStatusEnum.CURRENT)) {
            return getListBookingDTO(bookingRepository.getCurrentBookingsOfItemsOwner(userId));
        } else if (state.equals(BookingStatusEnum.PAST)) {
            return getListBookingDTO(bookingRepository.getPastBookingsOfItemsOwner(userId));
        } else if (state.equals(BookingStatusEnum.FUTURE)) {
            return getListBookingDTO(bookingRepository.getFutureBookingsOfItemsOwner(userId));
        } else if (state.equals(BookingStatusEnum.WAITING)) {
            return getListBookingDTO(bookingRepository.getBookingsOfItemsOwner(userId, BookingStatusEnum.WAITING));
        } else if (state.equals(BookingStatusEnum.REJECTED)) {
            return getListBookingDTO(bookingRepository.getBookingsOfItemsOwner(userId, BookingStatusEnum.REJECTED));
        } else
            throw new UnsupportedStatus(String.format("Booking State: %s", state));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID: %d is not existed", userId)));
    }

    private Item getItemById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Item with ID: %d is not existed", itemId)));
    }

    private void checkIfUserIsItemOwner(int userId, int ownerId) {
        if (ownerId == userId) {
            throw new ObjectNotFoundException("User can't book hiw own item");
        }
    }

    private void checkIfUserIsNotItemOwner(int userId, int ownerId) {
        if (ownerId != userId) {
            throw new ObjectNotFoundException(String
                    .format("User with ID: %d is not owner of Item with ID: %d", userId, ownerId));
        }
    }

    private void checkIfItemIsAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new IncorrectInputException("Item is unavailable");
        }
    }

    private void checkIfBookingEndDateIsAfterBookingStartDate(NewBookingDto newBookingDto) {
        if (newBookingDto.getEnd().isBefore(newBookingDto.getStart())) {
            throw new IncorrectInputException("Booking dates are incorrect");
        }
    }

    private Booking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Booking with ID: %d is not existed", bookingId)));
    }

    private void checkIfBookingStatusIsWaiting(Booking booking) {
        if (!booking.getStatus().equals(BookingStatusEnum.WAITING)) {
            throw new IncorrectInputException(String
                    .format("Booking ID: %d status is not %s", booking.getId(), BookingStatusEnum.WAITING));
        }
    }

    private BookingStatusEnum approveBookingOrReject(boolean isApproved) {
        if (isApproved) {
            return BookingStatusEnum.APPROVED;
        } else {
            return BookingStatusEnum.REJECTED;
        }
    }

    private void checkIfUserCanAccessToBooking(Booking booking, int userId) {
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw new ObjectNotFoundException(String
                    .format("User ID: %d can't access booking with ID: %d", userId, booking.getId()));
    }

    private BookingDto getBookingDto(Booking booking) {
        return bookingMapper.mapBookingToBookingDTO(bookingRepository.save(booking));
    }

    private List<BookingDto> getListBookingDTO(List<Booking> booking) {
        return booking.stream()
                .map(bookingMapper::mapBookingToBookingDTO)
                .collect(Collectors.toList());
    }

    private List<BookingDto> getPageBookingDTO(Page<Booking> booking) {
        return booking.stream()
                .map(bookingMapper::mapBookingToBookingDTO)
                .collect(Collectors.toList());
    }
}

