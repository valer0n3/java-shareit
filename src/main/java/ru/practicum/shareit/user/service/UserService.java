package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserPostDto;

import java.util.List;

public interface UserService {
    UserPostDto createUser(UserPostDto userPostDto);

    UserPostDto deleteUser(int id);

    UserPostDto updateUser(UserPostDto userPostDto, int id);

    List<User> getAllUsers();

    UserPostDto getUserById(int id);
}
