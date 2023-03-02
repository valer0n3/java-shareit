package ru.practicum.shareit.exception;

public class UnsupportedStatus extends RuntimeException {
    public UnsupportedStatus(String message) {
        super(message);
    }
}
