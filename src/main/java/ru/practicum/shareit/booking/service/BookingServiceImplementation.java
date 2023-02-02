package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;

@Controller
@AllArgsConstructor
public class BookingServiceImplementation implements BookingService {
    private final BookingRepository bookingRepository;
    @Override
    public BookingDto addNewBooking(BookingDto bookingDto, int userId) {
        return null;
    }

    @Override
    public BookingDto confirmBookingRequest(BookingDto bookingDto, int userId, int bookingId, boolean isApproved) {
        return null;
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        return null;
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
