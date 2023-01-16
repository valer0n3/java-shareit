package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        checkIfEmailExists(userDto.getEmail());
        return userStorage.createUser(userDto);
    }

    @Override
    public UserDto deleteUser(int id) {
        return userStorage.deleteUser(id)
                .orElseThrow(() -> new IncorrectInputException(String.format("Id %i is not existed", id)));
    }

    @Override
    public User updateUser(UserDto userDto, int id) {
        userDto.setId(id);
        checkIfIdExists(id);
        checkIfEmailExists(userDto.getEmail());
        return UserMapper.DtoToUser(userStorage.updateUser(userDto, id));
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public UserDto getUserById(int id) {
        checkIfIdExists(id);
        return userStorage.getUserById(id)
                .orElseThrow(() -> new IncorrectInputException(String.format("Id %i is not existed", id)));
    }

    public void checkIfIdExists(int id) {
        if (!userStorage.checkIfIdAlreadyExists(id)) {
            throw new IncorrectInputException(String.format("Id %i is not existed", id));
        }
    }

    public void checkIfEmailExists(String email) {
        if (userStorage.checkIfEmailAlreadyExists(email)) {
            throw new DuplicatedDataException(String.format("Email %s is already existed!", email));
        }
    }
}
