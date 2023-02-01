package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addNewBooking(BookingDto bookingDto, int userId);

    BookingDto confirmBookingRequest(BookingDto bookingDto, int userId, int bookingId, boolean isApproved);

    BookingDto getBookingById(int bookingId, int userId);

    BookingDto getAllBookingsOfCurrentUser(int userId, String state);

    BookingDto getAllBookingsOfAllUserItems(int userId, String state);
}
