package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.variables.Variables.X_SHARER_USER_ID;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addNewBooking(@Valid @RequestBody NewBookingDto newBookingDto,
                                                @RequestHeader(X_SHARER_USER_ID) int userId) {
        return bookingClient.addNewBooking(newBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmBookingRequest(
            @RequestHeader(X_SHARER_USER_ID) int userId,
            @PathVariable int bookingId,
            @RequestParam boolean approved) {
        return bookingClient.confirmBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable int bookingId, @RequestHeader(X_SHARER_USER_ID) int userId) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsOfCurrentUser(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                              @RequestParam(defaultValue = "all") String state,
                                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                                              @RequestParam(defaultValue = "10") @Min(1) int size) {
        return bookingClient.getAllBookingsOfCurrentUser(userId, BookingStatusEnum.checkIfStatusIsIncorrect(state),
                from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsOfAllUserItems(@Validated @RequestHeader(X_SHARER_USER_ID) int userId,
                                                               @RequestParam(defaultValue = "all") String state,
                                                               @RequestParam(defaultValue = "0") @Min(0) int from,
                                                               @RequestParam(defaultValue = "10") @Min(1) int size) {
        return bookingClient.getAllBookingsOfAllUserItems(userId, BookingStatusEnum.checkIfStatusIsIncorrect(state), from, size);
    }
}
