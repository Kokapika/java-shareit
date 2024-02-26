package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long generatorId = 1L;

    @Override
    public User addUser(User user) {
        checkEmail(user);
        emails.add(user.getEmail());
        user.setId(generatorId);
        users.put(generatorId, user);
        generatorId++;
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        checkUserInMemory(id);
        updateEmail(findUserById(id).getEmail(), user.getEmail());
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User findUserById(Long id) {
        checkUserInMemory(id);
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        checkUserInMemory(id);
        emails.remove(findUserById(id).getEmail());
        users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkEmail(User user) {
        if (emails.contains(user.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
        return true;
    }

    private void updateEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
        emails.add(newEmail);
    }

    private void checkUserInMemory(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
    }
}
