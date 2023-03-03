package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;
import ru.practicum.shareit.request.service.RequestServiceImplementation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.variables.Variables.X_SHARER_USER_ID;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    private RequestDto requestDto;
    private NewRequestDto newRequestDto;
    private RequestGetAllDto requestGetAllDto;
    private RequestGetAllDto requestGetAllDto2;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestServiceImplementation requestService;
    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    public void beforeEach() {
        requestDto = RequestDto.builder()
                .id(1)
                .build();
        newRequestDto = NewRequestDto.builder()
                .description("testDescription")
                .build();
        requestGetAllDto = RequestGetAllDto.builder()
                .id(2)
                .build();
        requestGetAllDto2 = RequestGetAllDto.builder()
                .id(2)
                .build();
    }

    @SneakyThrows
    @Test
    public void addNewRequest_whenInputIsValid_thenReturnOk() {
        int userId = 1;
        when(requestService.addNewRequest(newRequestDto, userId)).thenReturn(requestDto);
        String result = mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(newRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestDto), result);
        verify(requestService).addNewRequest(newRequestDto, userId);
    }

    @SneakyThrows
    @Test
    public void getOtherUsersRequests_whenCorrectInput_thenReturnOk() {
        int userId = 1;
        int index = 0;
        int size = 10;
        List<RequestGetAllDto> requestGetAllDtos = List.of(requestGetAllDto, requestGetAllDto2);
        when(requestService.getOtherUsersRequests(userId, index, size)).thenReturn(requestGetAllDtos);
        String result = mockMvc.perform(get("/requests/all", userId, index, size)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestGetAllDtos), result);
        verify(requestService).getOtherUsersRequests(userId, index, size);
    }

    @SneakyThrows
    @Test
    public void getOwnRequests() {
        int userId = 1;
        List<RequestGetAllDto> requestGetAllDtos = List.of(requestGetAllDto, requestGetAllDto2);
        when(requestService.getOwnRequests(userId)).thenReturn(requestGetAllDtos);
        String result = mockMvc.perform(get("/requests/", userId)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestGetAllDtos), result);
        verify(requestService).getOwnRequests(userId);
    }

    @SneakyThrows
    @Test
    public void getRequestWithAnswers() {
        int userId = 1;
        int requestId = 1;
        when(requestService.getRequestWithAnswers(userId, requestId)).thenReturn(requestGetAllDto);
        String result = mockMvc.perform(get("/requests/{id}", userId, requestId)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestGetAllDto), result);
        verify(requestService).getRequestWithAnswers(userId, requestId);
    }
}