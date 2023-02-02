package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(NewBookingDto newBookingDto, int userId);

    BookingDto confirmBookingRequest(int userId, int bookingId, boolean isApproved);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getAllBookingsOfCurrentUser(int userId, BookingStatusEnum state);

    List<BookingDto> getAllBookingsOfAllUserItems(int userId, BookingStatusEnum state);
}
