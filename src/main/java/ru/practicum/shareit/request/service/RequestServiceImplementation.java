package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;
import ru.practicum.shareit.request.dto.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImplementation implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public RequestDto addNewRequest(NewRequestDto newRequestDto, int userId) {
        User user = checkIfUserExists(userId);
        Request request = requestMapper.mapNewRequestDtoToRequest(newRequestDto);
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        return requestMapper.mapRequestToRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestGetAllDto> getOwnRequests(int userId) {
        checkIfUserExists(userId);
        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        List<Item> items = itemRepository.findAllByRequestIdIn(requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList()));
        return requests.stream()
                .map(request -> mapToRequestGetAllDto(request, items))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestGetAllDto> getOtherUsersRequests(int userId, int from, int size) {
        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("created").descending());
        Page<Request> requests = requestRepository.findByRequestorIdIsNot(userId, pageWithElements);
        List<Item> items = itemRepository.findAllByRequestIdIn(requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList()));
        return requests.stream()
                .map((request -> mapToRequestGetAllDto(request, items)))
                .collect(Collectors.toList());
    }

    @Override
    public RequestGetAllDto getRequestWithAnswers(int userId, int requestId) {
        checkIfUserExists(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String
                        .format("Request with id: %d is not existed", requestId)));
        List<ItemForRequestDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(itemMapper::mapItemToItemForRequestDto)
                .collect(Collectors.toList());
        return requestMapper.mapRequestToRequestGetAllDto(request, items);
    }

    private User checkIfUserExists(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User Id %d is not existed", userId)));
    }

    private RequestGetAllDto mapToRequestGetAllDto(Request request, List<Item> items) {
        List<ItemForRequestDto> itemsForRequestDto = items.stream()
                .filter(item -> item.getRequest().getId() == request.getId())
                .map(itemMapper::mapItemToItemForRequestDto)
                .collect(Collectors.toList());
        return requestMapper.mapRequestToRequestGetAllDto(request, itemsForRequestDto);
    }
}
