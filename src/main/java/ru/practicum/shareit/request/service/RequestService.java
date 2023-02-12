package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestAllOtherDTO;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;

import java.util.List;

public interface RequestService {
    RequestDto addNewRequest(NewRequestDto newRequestDto, int userId);

    List<RequestGetAllDto> getOwnRequests(int userId);

    List<RequestGetAllDto> getOtherUsersRequests(int userId, int from, int size);

    RequestGetAllDto getRequestWithAnswers(int userId, int requestId);
}
