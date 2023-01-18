package ru.practicum.shareit.user.dto.mapper;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserPostDto;

public class UserMapper {
    public static UserPostDto mapUserToUserPostDTO(User user) {
        return UserPostDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User mapUserPostDtoToUser(UserPostDto userPostDto) {
        return User.builder().
                id(userPostDto.getId())
                .name(userPostDto.getName())
                .email(userPostDto.getEmail())
                .build();
    }
}
