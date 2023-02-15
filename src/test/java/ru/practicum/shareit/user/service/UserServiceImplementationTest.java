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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplementationTest {
    private User testUser1;
    private User testUser2;
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
    public void beforeEachCreateUsers() {
        testUser1 = User.builder()
                .id(1)
                .name("testUser1")
                .email("testUser1@email.ru")
                .build();
        testUser2 = User.builder()
                .id(1)
                .name("testUser2")
                .email("testUser2@email.ru")
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
    void getUserById_whenUserFound_thenReturnUserPostDto() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(testUser1));
        Mockito.when(userMapper.mapUserToUserPostDTO(any())).thenReturn(userPostDto);
        UserPostDto actualUserPostDto = userService.getUserById(userId);
        System.out.println(actualUserPostDto);
        assertEquals(testUser1.getId(), actualUserPostDto.getId());
    }

    @Test
    void getUserById_whenUserNotFound_thenThrowObjectNotFoundException() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(userId));
        System.out.println(objectNotFoundException);
        assertEquals(String.format("Id %d is not existed", userId), objectNotFoundException.getMessage());
    }

    @Test
    void createUser_whenUserCreate_thenReturnUserPostDto() {
        int userId = 0;
        Mockito.when(userMapper.mapUserPostDtoToUser(any())).thenReturn(testUser1);
        Mockito.when(userRepository.save(testUser1)).thenReturn(testUser1);
        Mockito.when(userMapper.mapUserToUserPostDTO(any())).thenReturn(userPostDto);
        UserPostDto actualUserPostDto = userService.createUser(userPostDto);
        assertEquals(testUser1.getId(), actualUserPostDto.getId());
        verify(userRepository).save(testUser1);
    }

    @Test
    void updateUser_whenUserFound_thenUpdate() {
        int userId = 0;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(testUser1));
        Mockito.when(userMapper.mapUserToUserPatchDTO(any())).thenReturn(testUserPatchDto);
        UserPatchDto userPatchDto = userService.updateUser(testNewUserPatchDto, userId);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userPatchDto.getEmail(), userArgumentCaptor.getValue().getEmail());
    }
}