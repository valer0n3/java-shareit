package ru.practicum.shareit.booking.enums;

public enum BookingStatusEnum {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

    public static BookingStatusEnum
    transferStateToEnum(String stateInput) {
        switch (stateInput.toUpperCase()) {
            case "CURRENT":
                return BookingStatusEnum.CURRENT;
            case "PAST":
                return BookingStatusEnum.PAST;
            case "FUTURE":
                return BookingStatusEnum.FUTURE;
            case "WAITING":
                return BookingStatusEnum.WAITING;
            case "REJECTED":
                return BookingStatusEnum.REJECTED;
            case "ALL":
                return BookingStatusEnum.ALL;
        }
        return null;
    }
}
