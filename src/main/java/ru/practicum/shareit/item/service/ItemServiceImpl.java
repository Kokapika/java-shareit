package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserDao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userService;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = userService.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemDao.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Optional<Item> itemOptional = itemDao.findItemById(itemId);
        if (itemOptional.isPresent()) {
            if (!itemOptional.get().getOwner().getId().equals(userId)) {
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
            item.setOwner(itemFromStorage.getOwner());

            return ItemMapper.toItemDto(itemDao.updateItem(item));
        }
        return itemDto;
    }

    @Override
    public ItemDto findItemById(Long userId, Long itemId) {
        userService.findUserById(userId);
        Optional<Item> itemGet = itemDao.findItemById(itemId);
        itemGet.orElseThrow(() -> new NotFoundException(String.format("У пользователя с id = %s не " +
                "существует вещи с id = %s", userId, itemId)));
        return ItemMapper.toItemDto(itemGet.get());
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        userService.findUserById(userId);
        List<Item> itemList = itemDao.getAllItems(userId);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        userService.findUserById(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> itemList = itemDao.searchItem(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
