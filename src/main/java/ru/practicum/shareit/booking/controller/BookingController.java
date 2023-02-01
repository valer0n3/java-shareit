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
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addNewBooking(@Valid @RequestBody BookingDto bookingDto,
                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        return null;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBookingRequest(@RequestBody BookingDto bookingDto,
                                            @RequestHeader(X_SHARER_USER_ID) int userId,
                                            @PathVariable int bookingId,
                                            @RequestParam boolean isApproved) {
        return null;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable int bookingId, @RequestHeader(X_SHARER_USER_ID) int userId) {
        return null;
    }

    @GetMapping
    public BookingDto getAllBookingsOfCurrentUser(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @RequestParam String state) {
        return null;
    }

    @GetMapping("/owner")
    public BookingDto getAllBookingsOfAllUserItems(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                   @RequestParam String state) {
        return null;
    }
}
