package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.service.BookingServiceImplementation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private BookingDto bookingDto;
    private NewBookingDto newBookingDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingServiceImplementation bookingService;
    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void beforeEachCreateUsers() {
        bookingDto = BookingDto.builder()
                .id(1)
                .build();
        newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .build();
    }

    @SneakyThrows
    @Test
    void addNewBooking_whenInputIsCorrect_thenResponseOk() {
        int userId = 1;
        when(bookingService.addNewBooking(newBookingDto, userId)).thenReturn(bookingDto);
        String result = mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(newBookingDto), result);
        verify(bookingService).addNewBooking(newBookingDto, userId);
    }

    @SneakyThrows
    @Test
    void confirmBookingRequest() {
        int userId = 1;
        int bookingId = 1;
        boolean approved = true;
        when(bookingService.confirmBookingRequest(userId, bookingId, approved)).thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/{bookingId}/?approved={approved}", bookingId, approved)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(bookingService).confirmBookingRequest(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        int bookingId = 1;
        int userId = 1;
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(bookingService).getBookingById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllBookingsOfCurrentUser() {
        int userId = 1;
        int from = 0;
        int size = 10;
        List<BookingDto> bookingDtos = List.of(bookingDto);
        when(bookingService.getAllBookingsOfCurrentUser(userId, BookingStatusEnum.ALL, from, size))
                .thenReturn(bookingDtos);
        mockMvc.perform(get("/bookings/")
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(bookingService).getAllBookingsOfCurrentUser(userId, BookingStatusEnum.ALL, from, size);
    }

    @SneakyThrows
    @Test
    void getAllBookingsOfAllUserItems() {
        int userId = 1;
        int from = 0;
        int size = 10;
        List<BookingDto> bookingDtos = List.of(bookingDto);
        when(bookingService.getAllBookingsOfAllUserItems(userId, BookingStatusEnum.ALL, from, size))
                .thenReturn(bookingDtos);
        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(bookingService).getAllBookingsOfAllUserItems(userId, BookingStatusEnum.ALL, from, size);
    }
}