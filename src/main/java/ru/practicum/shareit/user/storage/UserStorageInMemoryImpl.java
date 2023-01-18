package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Integer, User> userHashMap = new HashMap<>();
    private static int id;

    public Map<Integer, User> getUserHashMap() {
        return userHashMap;
    }

    @Override
    public User createUser(User user) {
        user.setId(idGenerator());
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> deleteUser(int id) {
        return Optional.ofNullable(userHashMap.remove(id));
    }

    @Override
    public User updateUser(User user, int id) {
        if (user.getName() != null) {
            userHashMap.get(id).setName(user.getName());
        }
        if (user.getEmail() != null) {
            userHashMap.get(id).setEmail(user.getEmail());
        }
        System.out.println(userHashMap.get(id));
        return userHashMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userHashMap.get(id));
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
