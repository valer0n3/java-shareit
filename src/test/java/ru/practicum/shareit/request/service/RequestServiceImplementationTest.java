package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplementationTest {
    @InjectMocks
    private RequestServiceImplementation requestService;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;
    private RequestDto requestDto;
    private NewRequestDto newRequestDto;
    private Request request;
    private Request request2;
    private User user;
    private Item item;
    private Item item2;
    private ItemForRequestDto itemForRequestDto;

    @BeforeEach
    public void beforeEachCreateRequests() {
        requestDto = RequestDto.builder()
                .id(1)
                .description("testDescription")
                .build();
        newRequestDto = NewRequestDto.builder()
                .description("newTestDescription")
                .build();
        request = Request.builder()
                .id(1)
                .description("requestDescription")
                .build();
        request2 = Request.builder()
                .id(1)
                .description("request2Description")
                .build();
        user = User.builder()
                .id(1)
                .email("user@email")
                .name("testUser")
                .build();
        item = Item.builder()
                .id(1)
                .name("testItem")
                .build();
        item2 = Item.builder()
                .id(1)
                .name("testItem2")
                .build();
        itemForRequestDto = ItemForRequestDto.builder()
                .requestId(1)
                .build();
    }

    @Test
    void addNewRequest_whenUserExists_thenSave() {
        int userId = 0;
        request.setId(1);
        request.setDescription(newRequestDto.getDescription());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestMapper.mapNewRequestDtoToRequest(newRequestDto)).thenReturn(request);
        requestService.addNewRequest(newRequestDto, userId);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertNotNull(requestArgumentCaptor.getValue().getCreated());
        assertEquals(user, requestArgumentCaptor.getValue().getRequestor());
        verify(requestRepository).save(any());
    }

    @Test
    void addNewRequest_whenUserIsNotExisted_thenThrowObjectNotFoundException() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> requestService.addNewRequest(newRequestDto, userId));
        assertEquals(String.format("User Id %d is not existed", userId), objectNotFoundException.getMessage());
    }

    @Test
    void getOwnRequests_whenInputIsCorrect_thenGetList() {
        int userId = 0;
        List<Request> lr = List.of(request, request2);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findByRequestorIdOrderByCreatedDesc(userId)).thenReturn(lr);
        item.setRequest(request);
        item2.setRequest(request2);
        List<Item> itemList = List.of(item, item2);
        when(itemRepository.findAllByRequestIdIn(any())).thenReturn(itemList);
        requestService.getOwnRequests(userId);
        verify(itemRepository).findAllByRequestIdIn(any());
        verify(requestMapper, times(2)).mapRequestToRequestGetAllDto(any(), any());
    }

    @Test
    void getOtherUsersRequests_whenInputIsCorrect_thenGetList() {
        int userId = 0;
        int from = 1;
        int size = 10;
        item.setRequest(request);
        item2.setRequest(request2);
        PageImpl page = new PageImpl<>(List.of(request, request2));
        when(requestRepository.findByRequestorIdIsNot(anyInt(), any(Pageable.class))).thenReturn(page);
        when(itemRepository.findAllByRequestIdIn(any())).thenReturn(List.of(item, item2));
        requestService.getOtherUsersRequests(userId, from, size);
        verify(itemRepository).findAllByRequestIdIn(any());
        verify(requestMapper, times(2)).mapRequestToRequestGetAllDto(any(), any());
    }

    @Test
    void getRequestWithAnswers_whenUserIsNotExisted_thenTrowObjectNotFoundException() {
        int userId = 0;
        int requestId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestWithAnswers(userId, requestId));
        assertEquals(String.format("User Id %d is not existed", userId), objectNotFoundException.getMessage());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ObjectNotFoundException objectNotFoundException2 = assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestWithAnswers(userId, requestId));
    }

    @Test
    void getRequestWithAnswers_whenRequestIsCorrect_thenReturnRequestGetAllDto() {
        int userId = 0;
        int requestId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        requestService.getRequestWithAnswers(userId, requestId);
        verify(requestMapper).mapRequestToRequestGetAllDto(any(), anyList());
    }
}