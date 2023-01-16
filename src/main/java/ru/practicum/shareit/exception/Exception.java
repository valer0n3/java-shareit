package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Exception {
    private String error;
    private String description;

    public Exception(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
