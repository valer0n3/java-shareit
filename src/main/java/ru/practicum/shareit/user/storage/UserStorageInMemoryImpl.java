package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Integer, UserPostDto> userHashMap = new HashMap<>();
    private static int id;

    @Override
    public UserPostDto createUser(UserPostDto userPostDto) {
        userPostDto.setId(idGenerator());
        userHashMap.put(userPostDto.getId(), userPostDto);
        return userPostDto;
    }

    @Override
    public Optional<UserPostDto> deleteUser(int id) {
        return Optional.ofNullable(userHashMap.remove(id));
    }

    @Override
    public UserPostDto updateUser(UserPostDto userPostDto, int id) {
        if (userPostDto.getName() != null) {
            userHashMap.get(id).setName(userPostDto.getName());
        }
        if (userPostDto.getEmail() != null) {
            userHashMap.get(id).setEmail(userPostDto.getEmail());
        }
        System.out.println(userHashMap.get(id));
        return userHashMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userHashMap.values().stream().map((user) -> UserMapper.DtoToUser(user)).collect(Collectors.toList());
    }

    @Override
    public Optional<UserPostDto> getUserById(int id) {
        return Optional.ofNullable(userHashMap.get(id));
    }

    @Override
    public boolean checkIfEmailAlreadyExists(String mail) {
        for (UserPostDto userPostDto : userHashMap.values()) {
            if (userPostDto.getEmail().equalsIgnoreCase(mail)) {
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
