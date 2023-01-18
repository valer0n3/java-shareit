package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    User deleteUser(int id);

    User updateUser(User user, int id);

    List<User> getAllUsers();

    User getUserById(int id);

    boolean checkIfEmailAlreadyExists(String mail);

    boolean checkIfIdAlreadyExists(int id);
}
