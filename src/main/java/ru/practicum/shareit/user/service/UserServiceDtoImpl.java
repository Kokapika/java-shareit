package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceDtoImpl implements UserServiceDto {
    private final UserServiceDao userServiceDao;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkEmail(user);
        return UserMapper.toUserDto(userServiceDao.addUser(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
        User user = new User();
        UserDto userFromMemory = findUserById(id);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        } else {
            user.setName(userFromMemory.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(user);
            user.setEmail(userDto.getEmail());
        } else {
            user.setEmail(userFromMemory.getEmail());
        }
        user.setId(id);
        return UserMapper.toUserDto(userServiceDao.updateUser(id, user));
    }

    @Override
    public UserDto findUserById(Long id) {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
        User user = userServiceDao.findUserById(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (isUserInMemory(id)) {
            userServiceDao.deleteUserById(id);
        } else {
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userServiceDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkEmail(User user) {
        boolean isEmailNotUnique = userServiceDao.getAllUsers().stream()
                .anyMatch(thisUser -> thisUser.getEmail().equals(user.getEmail())
                        && !thisUser.getId().equals(user.getId()));
        if (isEmailNotUnique) {
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
    }

    private boolean isUserInMemory(Long userId) {
        return userServiceDao.getAllUsers().stream()
                .anyMatch(user -> user.getId().equals(userId));
    }
}
