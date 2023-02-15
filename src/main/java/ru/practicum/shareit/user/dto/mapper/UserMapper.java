package ru.practicum.shareit.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  //  UserMapper test = Mappers.getMapper(UserMapper.class);
    public UserPostDto mapUserToUserPostDTO(User user);

    public User mapUserPostDtoToUser(UserPostDto userPostDto);

    public UserPatchDto mapUserToUserPatchDTO(User user);
}
