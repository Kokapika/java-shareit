package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserServiceDaoImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceDtoImpl implements ItemServiceDto {
    private final ItemServiceDao itemServiceDao;
    private final UserServiceDaoImpl userServiceDaoImpl;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        UserDto user = UserMapper.toUserDto(userServiceDaoImpl.findUserById(userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId((UserMapper.toUser(user)).getId());
        return ItemMapper.toItemDto(itemServiceDao.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Optional<Item> itemOptional = itemServiceDao.findItemById(itemId);
        if (itemOptional.isPresent()) {
            if (!itemOptional.get().getOwnerId().equals(userId)) {
                throw new NotFoundException(String.format("Пользователь с id = %s " +
                        "не является владельцем вещи с id = %s", userId, itemId));
            }
            Item itemFromStorage = itemOptional.get();
            Item item = ItemMapper.toItem(itemDto);
            if (Objects.isNull(item.getAvailable())) {
                item.setAvailable(itemFromStorage.getAvailable());
            }
            if (Objects.isNull(item.getDescription())) {
                item.setDescription(itemFromStorage.getDescription());
            }
            if (Objects.isNull(item.getName())) {
                item.setName(itemFromStorage.getName());
            }
            item.setId(itemFromStorage.getId());
            item.setRequest(itemFromStorage.getRequest());
            item.setOwnerId(itemFromStorage.getOwnerId());

            return ItemMapper.toItemDto(itemServiceDao.updateItem(item));
        }
        return itemDto;
    }

    @Override
    public ItemDto findItemById(Long userId, Long itemId) {
        userServiceDaoImpl.findUserById(userId);
        Optional<Item> itemGet = itemServiceDao.findItemById(itemId);
        if (itemGet.isEmpty()) {
            throw new NotFoundException(String.format("У пользователя с id = %s не " +
                    "существует вещи с id = %s", userId, itemId));
        }
        return ItemMapper.toItemDto(itemGet.get());
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        userServiceDaoImpl.findUserById(userId);
        List<Item> itemList = itemServiceDao.getAllItems(userId);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        userServiceDaoImpl.findUserById(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> itemList = itemServiceDao.searchItem(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
