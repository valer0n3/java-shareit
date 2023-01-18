package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserPostDto createUser(UserPostDto userPostDto) {
        checkIfEmailExists(userPostDto.getEmail());
        User user = UserMapper.mapUserPostDtoToUser(userPostDto);
        return UserMapper.mapUserToUserPostDTO(userStorage.createUser(user));
    }

    @Override
    public UserPostDto deleteUser(int id) {
        return UserMapper.mapUserToUserPostDTO(userStorage.deleteUser(id));
    }

    @Override
    public UserPatchDto updateUser(UserPatchDto userPatchDto, int id) {
        checkIfIdExists(id);
        checkIfEmailExists(userPatchDto.getEmail());
        User user = UserMapper.mapUserPatchDtoToUser(userPatchDto);
        return UserMapper.mapUserToUserPatchDTO(userStorage.updateUser(user, id));
    }

    @Override
    public List<UserPostDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::mapUserToUserPostDTO).collect(Collectors.toList());
    }

    @Override
    public UserPostDto getUserById(int id) {
        checkIfIdExists(id);
        return UserMapper.mapUserToUserPostDTO(userStorage.getUserById(id));
    }

    public void checkIfIdExists(int id) {
        if (!userStorage.checkIfIdAlreadyExists(id)) {
            throw new IncorrectInputException(String.format("Id %d is not existed", id));
        }
    }

    public void checkIfEmailExists(String email) {
        if (userStorage.checkIfEmailAlreadyExists(email)) {
            throw new DuplicatedDataException(String.format("Email %s is already existed!", email));
        }
    }
}
