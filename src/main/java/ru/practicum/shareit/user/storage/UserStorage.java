package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    UserDto createUser(UserDto userDto);

    Optional<UserDto> deleteUser(int id);

    UserDto updateUser(UserDto userDto, int id);

    List<User> getAllUsers();

    Optional<UserDto> getUserById(int id);

    boolean checkIfEmailAlreadyExists(String mail);

    boolean checkIfIdAlreadyExists(int id);
}
