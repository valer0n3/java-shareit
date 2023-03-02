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
    public Exception incorrectValue(final IncorrectInputException incorrectInputException) {
        log.warn("Error 400: {}", incorrectInputException.getMessage());
        return new Exception("IncorrectInputException ", incorrectInputException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception objectNotFound(final ObjectNotFoundException objectNotFoundException) {
        log.warn("Error 404: {}", objectNotFoundException.getMessage());
        return new Exception("ObjectNotFoundException ", objectNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Exception duplicatedDataException(final DuplicatedDataException duplicatedDataException) {
        log.warn("Error 409: {}", duplicatedDataException.getMessage());
        return new Exception("DuplicatedDataException ", duplicatedDataException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception unsupportedStatus(final UnsupportedStatus unsupportedStatus) {
        log.warn("Error 400: {}", unsupportedStatus.getMessage());
        return Exception.builder().error(unsupportedStatus.getMessage()).build();
    }
}
