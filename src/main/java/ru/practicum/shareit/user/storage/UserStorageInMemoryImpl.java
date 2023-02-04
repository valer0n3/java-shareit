package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectInputException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Integer, User> userHashMap = new HashMap<>();
    private static int id;

    public final Map<Integer, User> getUserHashMap() {
        return userHashMap;
    }

    @Override
    public User createUser(User user) {
        user.setId(idGenerator());
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(int id) {
        return Optional.ofNullable(userHashMap.remove(id))
                .orElseThrow(() -> new IncorrectInputException(String.format("Id %d is not existed", id)));
    }

    @Override
    public User updateUser(User user, int id) {
        if (user.getName() != null) {
            userHashMap.get(id).setName(user.getName());
        }
        if (user.getEmail() != null) {
            userHashMap.get(id).setEmail(user.getEmail());
        }
        return userHashMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public User getUserById(int id) {
        return Optional.ofNullable(userHashMap.get(id))
                .orElseThrow(() -> new IncorrectInputException(String.format("Id %d is not existed", id)));
    }

    @Override
    public boolean checkIfEmailAlreadyExists(String mail) {
        for (User user : userHashMap.values()) {
            if (user.getEmail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        return false;
    }

    private int idGenerator() {
        return ++id;
    }

    public boolean checkIfIdAlreadyExists(int id) {
        return userHashMap.containsKey(id);
    }
}
