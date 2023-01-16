package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserStorage userStorage;

    @Override
    public User createUser(UserDto userDto) {
        return userStorage.createUser(userDto);
    }

    @Override
    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    @Override
    public User updateUser(UserDto userDto) {
        return userStorage.updateUser(userDto);
    }

    @Override
    public List<User> getAllUsers() {
        return getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return getUserById(id);
    }
}
