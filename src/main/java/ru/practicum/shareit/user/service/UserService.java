package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;

import java.util.List;

public interface UserService {
    UserPostDto createUser(UserPostDto userPostDto);

    UserPostDto deleteUser(int id);

    UserPatchDto updateUser(UserPatchDto userPatchDto, int id);

    List<UserPostDto> getAllUsers();

    UserPostDto getUserById(int id);
}
