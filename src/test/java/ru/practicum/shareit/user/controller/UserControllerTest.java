package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    @InjectMocks
    private UserController userController;
    private User testUser1;
    private User testUser2;
    private UserPostDto userPostDto;
    private UserPatchDto testUserPatchDto;
    private UserPatchDto testNewUserPatchDto;

    @BeforeEach
    public void beforeEachCreateUsers() {
        testUser1 = User.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testUser2 = User.builder()
                .id(1)
                .name("testUser2")
                .email("testUser2@email.ru")
                .build();
        userPostDto = UserPostDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testUserPatchDto = UserPatchDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testNewUserPatchDto = UserPatchDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
    }

    @SneakyThrows
    @Test
    void createUser() {
        when(userService.createUser(any())).thenReturn(userPostDto);
        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userPostDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userPostDto), result);
    }

    @Test
    void deleteUser() {
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserIsNotValid_thenReturnBadRequest() {
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

    @Test
    void getAllUsers() {
    }

    @SneakyThrows
    @Test
    void getUserById() {
        int userId = 1;
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).getUserById(userId);
    }
}