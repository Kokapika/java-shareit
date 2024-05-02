package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userServiceDtoImpl;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userServiceDtoImpl.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Long userId) {
        return userServiceDtoImpl.findUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceDtoImpl.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return userServiceDtoImpl.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userServiceDtoImpl.deleteUserById(userId);
    }
}
