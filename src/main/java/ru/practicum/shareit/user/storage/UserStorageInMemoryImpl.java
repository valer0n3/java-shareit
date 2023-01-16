package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Integer, UserDto> userHashMap = new HashMap<>();
    private static int id;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(idGenerator());
        userHashMap.put(userDto.getId(), userDto);
        return userDto;
    }

    @Override
    public Optional<UserDto> deleteUser(int id) {
        return Optional.ofNullable(userHashMap.remove(id));
    }

    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        if (userDto.getName() != null) {
            userHashMap.get(id).setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userHashMap.get(id).setEmail(userDto.getEmail());
        }
        System.out.println(userHashMap.get(id));
        return userHashMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userHashMap.values().stream().map((user) -> UserMapper.DtoToUser(user)).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(int id) {
        return Optional.ofNullable(userHashMap.get(id));
    }

    @Override
    public boolean checkIfEmailAlreadyExists(String mail) {
        for (UserDto userDto : userHashMap.values()) {
            if (userDto.getEmail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        return false;
    }

    private int idGenerator() {
        return ++id;
    }

    public boolean checkIfIdAlreadyExists(int id) {
        return userHashMap.containsKey(id);
    }
}
