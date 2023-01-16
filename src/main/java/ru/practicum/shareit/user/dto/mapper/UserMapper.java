package ru.practicum.shareit.user.dto.mapper;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder().build();
    }
}
