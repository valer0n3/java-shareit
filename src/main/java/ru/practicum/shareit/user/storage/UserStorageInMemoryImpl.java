package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Integer, UserDto> userHashMap;
    private static int id;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(idGenerator());
        userHashMap.put(userDto.getId(), userDto);
        return userDto;
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

    @Override
    public boolean checkIfEmailAlreadyExists(String mail) {
        return userHashMap.containsValue(mail);
    }

    public int idGenerator() {
        return ++id;
    }
}
