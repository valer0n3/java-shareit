package ru.practicum.shareit.user.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @InjectMocks
    private UserMapperImpl userMapper;
    private User newUser;
    private UserPostDto newUserPostDto;

    @BeforeEach
    public void beforeEach() {
        newUser = User.builder()
                .id(1)
                .name("userName")
                .build();
        newUserPostDto = UserPostDto.builder()
                .id(1)
                .name("userName")
                .build();
    }

    @Test
    public void mapUserToUserPostDTO() {
        UserPostDto userPostDto = userMapper.mapUserToUserPostDTO(newUser);
        assertEquals(newUser.getName(), userPostDto.getName());
    }

    @Test
    public void mapUserPostDtoToUser() {
        User user = userMapper.mapUserPostDtoToUser(newUserPostDto);
        assertEquals(newUserPostDto.getName(), newUser.getName());
    }

    @Test
    public void mapUserToUserPatchDTO() {
        UserPatchDto userPatchDto = userMapper.mapUserToUserPatchDTO(newUser);
        assertEquals(newUser.getName(), userPatchDto.getName());
    }
}