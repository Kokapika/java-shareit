package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User updateUser(Long id, User user);

    User findUserById(Long id);

    void deleteUserById(Long id);

    List<User> getAllUsers();

    boolean checkEmail(User user);
}
