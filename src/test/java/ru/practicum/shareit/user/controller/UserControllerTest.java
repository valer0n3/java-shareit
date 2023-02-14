package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void createUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers_whenInvoked_thenResponseWithUserCollection() {
        List<UserPostDto> expectedUsers = List.of(new UserPostDto());
        Mockito.when(userService.getAllUsers()).thenReturn(expectedUsers);
        List<UserPostDto> allUsers = userController.getAllUsers();
        System.out.println(expectedUsers + "; " + allUsers);
        assertEquals(expectedUsers, allUsers);
    }

    @Test
    void getUserById() {
    }
}