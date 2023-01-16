package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto deleteUser(int id);

    User updateUser(UserDto userDto, int id);

    List<User> getAllUsers();

    UserDto getUserById(int id);
}
