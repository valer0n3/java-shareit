package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    @Override
    public User createUser(UserDto userDto) {
        return null;
    }

    @Override
    public User deleteUser(int id) {
        return null;
    }

    @Override
    public User updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }
}
