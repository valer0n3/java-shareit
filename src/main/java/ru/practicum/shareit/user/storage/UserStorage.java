package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserPostDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    UserPostDto createUser(UserPostDto userPostDto);

    Optional<UserPostDto> deleteUser(int id);

    UserPostDto updateUser(UserPostDto userPostDto, int id);

    List<User> getAllUsers();

    Optional<UserPostDto> getUserById(int id);

    boolean checkIfEmailAlreadyExists(String mail);

    boolean checkIfIdAlreadyExists(int id);
}
