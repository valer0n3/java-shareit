package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImplementation implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestDto addNewRequest(NewRequestDto newRequestDto, int userId) {
        User user = checkIfUserExists(userId);
        Request request = requestMapper.mapNewRequestDtoToRequest(newRequestDto);
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        return requestMapper.mapRequestToRequestDto(requestRepository.save(request));
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

    private User checkIfUserExists(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
    }
}
