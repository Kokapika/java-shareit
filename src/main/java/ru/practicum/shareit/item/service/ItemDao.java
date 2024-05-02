package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item addItem(Item item);

    Item updateItem(Item item);

    Optional<Item> findItemById(Long itemId);

    List<Item> getAllItems(Long userId);

    List<Item> searchItem(String text);
}
