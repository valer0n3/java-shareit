package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception incorrectValue(final IncorrectInputException e) {
        log.warn("Error 400: " + e.getMessage());
        return new Exception("IncorrectInputException ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception objectNotFound(final ObjectNotFoundException e) {
        log.warn("Error 404: " + e.getMessage());
        return new Exception("ObjectNotFoundException ", e.getMessage());
    }
}
