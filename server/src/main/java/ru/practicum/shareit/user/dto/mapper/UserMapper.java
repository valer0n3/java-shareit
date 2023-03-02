package ru.practicum.shareit.user.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserPostDto mapUserToUserPostDTO(User user);

    public User mapUserPostDtoToUser(UserPostDto userPostDto);

    public UserPatchDto mapUserToUserPatchDTO(User user);
}
