package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {
    private User testUser1;
    private UserPostDto userPostDto;
    private UserPatchDto testUserPatchDto;
    private UserPatchDto testNewUserPatchDto;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImplementation userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    public void beforeEach() {
        testUser1 = User.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        userPostDto = UserPostDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testUserPatchDto = UserPatchDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testNewUserPatchDto = UserPatchDto.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
    }

    @Test
    public void getUserById_whenUserFound_thenReturnUserPostDto() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(testUser1));
        Mockito.when(userMapper.mapUserToUserPostDTO(any())).thenReturn(userPostDto);
        UserPostDto actualUserPostDto = userService.getUserById(userId);
        assertEquals(testUser1.getId(), actualUserPostDto.getId());
    }

    @Test
    public void getUserById_whenUserNotFound_thenThrowObjectNotFoundException() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals(String.format("Id %d is not existed", userId), objectNotFoundException.getMessage());
    }

    @Test
    public void createUser_whenUserCreate_thenReturnUserPostDto() {
        int userId = 0;
        Mockito.when(userMapper.mapUserPostDtoToUser(any())).thenReturn(testUser1);
        Mockito.when(userRepository.save(testUser1)).thenReturn(testUser1);
        Mockito.when(userMapper.mapUserToUserPostDTO(any())).thenReturn(userPostDto);
        UserPostDto actualUserPostDto = userService.createUser(userPostDto);
        assertEquals(testUser1.getId(), actualUserPostDto.getId());
        verify(userRepository).save(testUser1);
    }

    @Test
    public void updateUser_whenUserNotFound_thenThrowNotFoundException() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> userService.updateUser(testNewUserPatchDto, userId));
        assertEquals(String.format("Id %d is not existed", userId), objectNotFoundException.getMessage());
    }

    @Test
    public void updateUser_whenUserFound_thenUpdate() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(testUser1));
        Mockito.when(userMapper.mapUserToUserPatchDTO(any())).thenReturn(testUserPatchDto);
        UserPatchDto userPatchDto = userService.updateUser(testNewUserPatchDto, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userPatchDto.getEmail(), userArgumentCaptor.getValue().getEmail());
    }

    @Test
    public void updateUser_whenNameAndMailCorrect_thenUpdateNameAndEmail() {
        User oldUser = testUser1;
        int userId = 0;
        testNewUserPatchDto.setName("changedName");
        testNewUserPatchDto.setEmail("changed@email.com");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        userService.updateUser(testNewUserPatchDto, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(testNewUserPatchDto.getName(), userArgumentCaptor.getValue().getName());
        assertEquals(testNewUserPatchDto.getEmail(), userArgumentCaptor.getValue().getEmail());
        verify(userRepository).save(any());
    }

    @Test
    public void updateUser_whenNameisNullAndMailCorrect_thenUpdateOnlyMail() {
        User oldUser = testUser1;
        int userId = 0;
        testNewUserPatchDto.setName(null);
        testNewUserPatchDto.setEmail("changed@email.com");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        userService.updateUser(testNewUserPatchDto, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(oldUser.getName(), userArgumentCaptor.getValue().getName());
        assertEquals(testNewUserPatchDto.getEmail(), userArgumentCaptor.getValue().getEmail());
        verify(userRepository).save(any());
    }

    @Test
    public void updateUser_whenNameIsCorrectAndMailIsNull_thenUpdateOnlyName() {
        User oldUser = testUser1;
        int userId = 0;
        testNewUserPatchDto.setName("changedName");
        testNewUserPatchDto.setEmail(null);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        userService.updateUser(testNewUserPatchDto, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(testNewUserPatchDto.getName(), userArgumentCaptor.getValue().getName());
        assertEquals(oldUser.getEmail(), userArgumentCaptor.getValue().getEmail());
        verify(userRepository).save(any());
    }

    @Test
    public void deleteUser_whenUserExists_thenDeleteSuccessfully() {
        int userId = 0;
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void getAllUsers_whenUsersExists_thenReturnUserPostDtoList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserPostDto> usersList = userService.getAllUsers();
        verify(userRepository).findAll();
        assertTrue(usersList.isEmpty());
    }
}