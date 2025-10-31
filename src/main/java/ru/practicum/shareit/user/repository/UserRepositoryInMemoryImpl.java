package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private static Long lastId = 1L;

    private static Long generateNewId() {
        return lastId++;
    }

    @Override
    public User saveUser(User user) {
        Long newId = generateNewId();
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserByUserId(Long userId) {
        return users.get(userId);
    }

    @Override
    public User findUserByUserEmail(String userEmail) {
        return users.values().stream()
                .filter(el -> userEmail.equals(el.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }
}
