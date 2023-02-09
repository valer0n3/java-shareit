package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addNewBooking(@Valid @RequestBody NewBookingDto newBookingDto,
                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        return bookingService.addNewBooking(newBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBookingRequest(
            @RequestHeader(X_SHARER_USER_ID) int userId,
            @PathVariable int bookingId,
            @RequestParam boolean approved) {
        return bookingService.confirmBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable int bookingId, @RequestHeader(X_SHARER_USER_ID) int userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsOfCurrentUser(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                        @RequestParam(defaultValue = "all") String state) {
        return bookingService.getAllBookingsOfCurrentUser(userId, BookingStatusEnum.checkIfStatusIsIncorrect(state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsOfAllUserItems(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                         @RequestParam(defaultValue = "all") String state) {
        return bookingService.getAllBookingsOfAllUserItems(userId, BookingStatusEnum.checkIfStatusIsIncorrect(state));
    }
}
