package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    User createUser(UserDto userDto);

    User deleteUser(int id);

    User updateUser(UserDto userDto);

    List<User> getAllUsers();

    User getUserById(int id);
}
