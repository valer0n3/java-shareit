package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
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
        User booker = getUserById(userId);
        Booking booking = getBookingById(bookingId);
        checkIfUserIsNotItemOwner(userId, booking.getItem().getOwner().getId());
        checkIfBookingStatusIsWaiting(booking);
        booking.setStatus(approveBookingOrReject(isApproved, booking));
        return getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        Booking booking = getBookingById(bookingId);
        checkIfUserCanAccessToBooking(booking, userId);
        return getBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsOfCurrentUser(int userId, BookingStatusEnum state) {
        User booker = getUserById(userId);
        if (state.equals(BookingStatusEnum.ALL)) {
            return bookingRepository.getAllBookingsOfCurrentUser(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.CURRENT)) {
            return bookingRepository.getCurrentBookingsOfCurrentUser(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.PAST)) {
            return bookingRepository.getPastBookingsOfCurrentUser(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.FUTURE)) {
            return bookingRepository.getFutureBookingsOfCurrentUser(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.WAITING)) {
            return bookingRepository.getBookingsOfCurrentUser(userId, BookingStatusEnum.WAITING.name()).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.REJECTED)) {
            return bookingRepository.getBookingsOfCurrentUser(userId, BookingStatusEnum.REJECTED.name()).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else
            throw new UnsupportedStatus(String.format("Unknown state: %s", state));
    }

    @Override
    public List<BookingDto> getAllBookingsOfAllUserItems(int userId, BookingStatusEnum state) {
        User booker = getUserById(userId);
        if (state.equals(BookingStatusEnum.ALL)) {
            return bookingRepository.getAllBookingsOfItemsOwner(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.CURRENT)) {
            return bookingRepository.getCurrentBookingsOfItemsOwner(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.PAST)) {
            return bookingRepository.getPastBookingsOfItemsOwner(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.FUTURE)) {
            return bookingRepository.getFutureBookingsOfItemsOwner(userId).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.WAITING)) {
            return bookingRepository.getBookingsOfItemsOwner(userId, BookingStatusEnum.WAITING).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatusEnum.REJECTED)) {
            return bookingRepository.getBookingsOfItemsOwner(userId, BookingStatusEnum.REJECTED).stream()
                    .map(bookingMapper::mapBookingToBookingDTO)
                    .collect(Collectors.toList());
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

    public void checkIfItemIsAvailable(Item item) {
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

    private BookingStatusEnum approveBookingOrReject(boolean isApproved, Booking booking) {
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
}

