package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto addNewRequest(NewRequestDto newRequestDto, int userId);

    List<RequestDto> getOwnRequests(int userId);

    List<RequestDto> getOtherUsersRequests(int userId, int from, int size);

    RequestDto getRequestWithAnswers(int userId, int requestId);
}
