package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserPostDto createUser(UserPostDto userPostDto) {
        User user = userMapper.mapUserPostDtoToUser(userPostDto);
        return userMapper.mapUserToUserPostDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserPatchDto updateUser(UserPatchDto userPatchDto, int id) {
        User user = findUser(id);
        setNameAndEmail(userPatchDto, user);
        return userMapper.mapUserToUserPatchDTO(userRepository.save(user));
    }

    @Override
    public List<UserPostDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapUserToUserPostDTO).collect(Collectors.toList());
    }

    @Override
    public UserPostDto getUserById(int id) {
        return userMapper.mapUserToUserPostDTO(userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Id %d is not existed", id))));
    }

    private User findUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Id %d is not existed", userId)));
    }

    private void setNameAndEmail(UserPatchDto userPatchDto, User user) {
        if (userPatchDto.getName() != null) {
            user.setName(userPatchDto.getName());
        }
        if (userPatchDto.getEmail() != null) {
            user.setEmail(userPatchDto.getEmail());
        }
    }
}
