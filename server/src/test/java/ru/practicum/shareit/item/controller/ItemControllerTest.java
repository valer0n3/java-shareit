package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;
import ru.practicum.shareit.item.service.ItemServiceImplementation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.variables.Variables.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private ItemDto itemDto;
    private ItemPatchDto itemPatchDto;
    private ItemWithBookingDatesDto itemWithBookingDatesDto;
    private CommentDto commentDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemServiceImplementation itemService;
    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void beforeEach() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("testName")
                .description("testDescription")
                .available(true)
                .build();
        itemPatchDto = ItemPatchDto.builder()
                .id(1)
                .build();
        itemWithBookingDatesDto = ItemWithBookingDatesDto.builder()
                .id(1)
                .name("testName")
                .description("testDescription")
                .available(true)
                .build();
        commentDto = CommentDto.builder()
                .text("testText")
                .build();
    }

    @SneakyThrows
    @Test
    public void addNewItem_whenInputIsCorrect_thenResponseOk() {
        int userId = 1;
        when(itemService.addNewItem(itemDto, userId)).thenReturn(itemDto);
        String result = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
        verify(itemService).addNewItem(itemDto, userId);
    }

    @SneakyThrows
    @Test
    public void updateItem_whenInputIsCorrect_thenResponseOk() {
        int userId = 1;
        int itemId = 1;
        when(itemService.updateItem(itemPatchDto, userId, itemId)).thenReturn(itemPatchDto);
        String result = mockMvc.perform(patch("/items/{itemId}", userId, itemId)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemPatchDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemPatchDto), result);
        verify(itemService).updateItem(itemPatchDto, userId, itemId);
    }

    @SneakyThrows
    @Test
    public void getItemById_whenInputIsCorrect_thenResponseOk() {
        int itemId = 1;
        int userId = 1;
        when(itemService.getItemById(userId, itemId)).thenReturn(itemWithBookingDatesDto);
        String result = mockMvc.perform(get("/items/{itemId}", userId, itemId)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemWithBookingDatesDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemWithBookingDatesDto), result);
        verify(itemService).getItemById(userId, itemId);
    }

    @SneakyThrows
    @Test
    public void getAllItemsForOwner_whenInputIsCorrect_thenResponseOk() {
        int userId = 1;
        List<ItemWithBookingDatesDto> itemWithBookingDatesDto1 = List.of(itemWithBookingDatesDto);
        when(itemService.getAllItemsForOwner(userId)).thenReturn(itemWithBookingDatesDto1);
        String result = mockMvc.perform(get("/items", userId)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemWithBookingDatesDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemWithBookingDatesDto1), result);
        verify(itemService).getAllItemsForOwner(userId);
    }

    @SneakyThrows
    @Test
    public void searchItem_whenInputIsCorrect_thenResponseOk() {
        String inputText = "test";
        List<ItemDto> itemDtoList = List.of(itemDto);
        when(itemService.searchItem(inputText)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items/search?text={text}", inputText)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(inputText)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).searchItem(inputText);
    }

    @SneakyThrows
    @Test
    public void addComment_whenInputIsCorrect_thenResponseOk() {
        int itemId = 1;
        int userId = 1;
        when(itemService.addComment(commentDto, itemId, userId)).thenReturn(commentDto);
        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header(X_SHARER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(commentDto), result);
        verify(itemService).addComment(commentDto, itemId, userId);
    }
}