package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemServiceDaoImpl implements ItemServiceDao {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private Long generatorId = 1L;

    @Override
    public Item addItem(Item item) {
        List<Item> listItems = new ArrayList<>();
        item.setId(generatorId);
        listItems.add(item);
        items.put(item.getOwnerId(), listItems);
        generatorId++;
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        List<Item> userItems = items.get(item.getOwnerId());
        List<Item> toRemove = userItems.stream()
                .filter(userItem -> userItem.getId().equals(item.getId()))
                .collect(Collectors.toList());
        userItems.removeAll(toRemove);
        userItems.add(item);
        return item;
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return new ArrayList<>(items.get(userId));
    }

    @Override
    public List<Item> searchItem(String text) {
        String searchText = text.toLowerCase();
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
}
