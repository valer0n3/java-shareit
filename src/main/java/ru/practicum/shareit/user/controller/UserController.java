package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserPostDto createUser(@Valid @RequestBody UserPostDto userPostDto) {
        return userService.createUser(userPostDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UserPatchDto updateUser(@Valid @PathVariable int id,
                                   @RequestBody UserPatchDto userPatchDto) {
        return userService.updateUser(userPatchDto, id);
    }

    @GetMapping
    public List<UserPostDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserPostDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
