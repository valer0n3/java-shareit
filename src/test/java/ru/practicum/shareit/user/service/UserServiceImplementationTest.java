package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {
    @Mock
    private  UserMapper userMapper;
    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void getUserById() {

        Mockito.when(userRepository.findById(10)).thenReturn(Optional.of(new User()));
        System.out.println(userService.getUserById(10));

    }
}