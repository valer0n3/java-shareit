package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserServiceImplementation userService;
    private UserPostDto userPostDto;
    private UserPostDto userPostDto2;
    private UserPatchDto testNewUserPatchDto;

    @BeforeEach
    public void beforeEach() {
        userPostDto = UserPostDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        userPostDto2 = UserPostDto.builder()
                .id(2)
                .name("testUser2")
                .email("testUser2@email.ru")
                .build();
        testNewUserPatchDto = UserPatchDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
    }

    @SneakyThrows
    @Test
    public void createUser_whenSuccessfullyCreated_thenReturnOk() {
        when(userService.createUser(any())).thenReturn(userPostDto);
        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userPostDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userPostDto), result);
        verify(userService).createUser(userPostDto);
    }

    @SneakyThrows
    @Test
    public void deleteUser_whenUserIsValid_thenReturnOk() {
        int userId = 0;
        doNothing().when(userService).deleteUser(userId);
        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());
        verify(userService).deleteUser(userId);
    }

    @SneakyThrows
    @Test
    public void updateUser_whenUserIsValid_thenReturnOkRequest() {
        int userId = 1;
        when(userService.updateUser(any(), eq(1))).thenReturn(testNewUserPatchDto);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testNewUserPatchDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).updateUser(testNewUserPatchDto, userId);
        assertEquals(objectMapper.writeValueAsString(userPostDto), result);
    }

    @SneakyThrows
    @Test
    public void getAllUsers_whenIsValid_thenReturnOk() {
        List<UserPostDto> userPostDtoList = List.of(userPostDto, userPostDto2);
        when(userService.getAllUsers()).thenReturn(userPostDtoList);
        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).getAllUsers();
        assertEquals(objectMapper.writeValueAsString(userPostDtoList), result);
    }

    @SneakyThrows
    @Test
    public void getUserById() {
        int userId = 1;
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).getUserById(userId);
    }
}