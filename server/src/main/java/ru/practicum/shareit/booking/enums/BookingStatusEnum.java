package ru.practicum.shareit.booking.enums;

import ru.practicum.shareit.exception.UnsupportedStatus;

public enum BookingStatusEnum {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

/*    public static BookingStatusEnum checkIfStatusIsIncorrect(String state) {
        try {
            return BookingStatusEnum.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatus(String.format("Unknown state: %s", state));
        }
    }*/
}
