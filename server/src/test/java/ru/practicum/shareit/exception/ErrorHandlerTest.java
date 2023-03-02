package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    public void incorrectValue() {
        Exception exception = errorHandler.incorrectValue(new IncorrectInputException("testAlert"));
        assertEquals("testAlert", exception.getDescription());
    }

    @Test
    public void objectNotFound() {
        Exception exception = errorHandler.objectNotFound(new ObjectNotFoundException("testAlert"));
        assertEquals("testAlert", exception.getDescription());
    }

    @Test
    public void duplicatedDataException() {
        Exception exception = errorHandler.duplicatedDataException(new DuplicatedDataException("testAlert"));
        assertEquals("testAlert", exception.getDescription());
    }

    @Test
    public void unsupportedStatus() {
        Exception exception = errorHandler.unsupportedStatus(new UnsupportedStatus("testAlert"));
        assertEquals("testAlert", exception.getError());
    }
}