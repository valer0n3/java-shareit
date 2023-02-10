package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.storage.RequestRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImplementation implements RequestService{
    private final RequestRepository requestRepository;
    @Override
    public RequestDto addNewRequest(RequestDto requestDto, int userId) {
        return null;
    }

    @Override
    public List<RequestDto> getOwnRequests(int userId) {
        return null;
    }

    @Override
    public List<RequestDto> getOtherUsersRequests(int userId, int from, int size) {
        return null;
    }

    @Override
    public RequestDto getRequestWithAnswers(int userId, int requestId) {
        return null;
    }
}
