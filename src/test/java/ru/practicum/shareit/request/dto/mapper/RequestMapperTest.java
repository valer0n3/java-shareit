package ru.practicum.shareit.request.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;
import ru.practicum.shareit.request.model.Request;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    @InjectMocks
    private RequestMapperImpl requestMapper;
    private Request newRequest;
    private RequestGetAllDto newRequestGetAllDto;
    private NewRequestDto newRequestDto;

    @BeforeEach
    public void beforeEach() {
        newRequest = Request.builder()
                .id(1)
                .description("newRequest")
                .build();
        newRequestGetAllDto = RequestGetAllDto.builder()
                .id(1)
                .description("newRequest")
                .build();
        newRequestDto = NewRequestDto.builder()
                .description("newRequest")
                .build();
    }

    @Test
    public void mapNewRequestDtoToRequest() {
        Request request = requestMapper.mapNewRequestDtoToRequest(newRequestDto);
        assertEquals(newRequestDto.getDescription(), request.getDescription());
    }

    @Test
    public void mapRequestToRequestDto() {
        RequestDto requestDto = requestMapper.mapRequestToRequestDto(newRequest);
        assertEquals(newRequest.getDescription(), requestDto.getDescription());
    }

    @Test
    public void mapRequestToRequestGetAllDto() {
        RequestGetAllDto requestGetAllDto = requestMapper.mapRequestToRequestGetAllDto(newRequest, Collections.emptyList());
        assertEquals(newRequest.getDescription(), requestGetAllDto.getDescription());
    }
}