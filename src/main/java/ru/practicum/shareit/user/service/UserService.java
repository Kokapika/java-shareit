package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto findUserById(Long id);

    void deleteUserById(Long id);

    List<UserDto> getAllUsers();
}
