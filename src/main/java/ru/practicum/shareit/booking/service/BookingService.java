package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

public interface BookingService {
    BookingDto addNewBooking(NewBookingDto newBookingDto, int userId);

    BookingDto confirmBookingRequest(int userId, int bookingId, boolean isApproved);

    BookingDto getBookingById(int bookingId, int userId);

    BookingDto getAllBookingsOfCurrentUser(int userId, String state);

    BookingDto getAllBookingsOfAllUserItems(int userId, String state);
}
