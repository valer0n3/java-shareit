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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Controller
@AllArgsConstructor
public class BookingServiceImplementation implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addNewBooking(NewBookingDto newBookingDto, int userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID: %d is not existed", userId)));
        Item item = itemRepository.findById(newBookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Item with ID: %d is not existed", newBookingDto.getItemId())));
        System.out.println("********---------------" + item.getOwner().getId() + " and " + userId);
        if (item.getOwner().getId() == userId) {
            throw new IncorrectInputException(String.format("User can't book hiw own item"));
        }
        if (item.getAvailable() == false) {
            throw new ObjectNotFoundException("Item is unavailable");
        }
        Booking booking = bookingMapper.mapNewBookingDtoToBooking(newBookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatusEnum.WAITING);
        return bookingMapper.mapBookingToBookingDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDto confirmBookingRequest(int userId, int bookingId, boolean isApproved) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("User with ID: %d is not existed", userId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Booking with ID: %d is not existed", bookingId)));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new IncorrectInputException(String
                    .format("User with ID: %d is not owner of Item with ID: %d", userId, booking.getItem().getId()));
        }
        if (!booking.getStatus().equals(BookingStatusEnum.WAITING)) {
            throw new IncorrectInputException(String
                    .format("Booking ID: %d status is not %s", bookingId, BookingStatusEnum.WAITING));
        }
        if (isApproved) {
            booking.setStatus(BookingStatusEnum.APPROVED);
        } else {
            booking.setStatus(BookingStatusEnum.REJECTED);
        }
        return bookingMapper.mapBookingToBookingDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Booking with ID: %d is not existed", bookingId)));
        if (booking.getBooker().getId() != userId || booking.getItem().getOwner().getId() != userId) {
            throw new IncorrectInputException(String
                    .format("User ID: %d can't access booking with ID: %d", userId, bookingId));
        }
        return bookingMapper.mapBookingToBookingDTO(booking);
    }

    @Override
    public BookingDto getAllBookingsOfCurrentUser(int userId, String state) {
        return null;
    }

    @Override
    public BookingDto getAllBookingsOfAllUserItems(int userId, String state) {
        return null;
    }
}
