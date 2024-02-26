package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {
    private final Map<Long, List<Item>> userItems = new HashMap<>();
    private final Map<Long, Item> allItems = new HashMap<>();
    private Long generatorId = 1L;

    @Override
    public Item addItem(Item item) {
        Long ownerId = item.getOwner().getId();
        List<Item> listItems = new ArrayList<>();
        if (userItems.containsKey(ownerId)) {
            listItems = userItems.get(ownerId);
        }
        item.setId(generatorId);
        listItems.add(item);
        userItems.put(ownerId, listItems);
        allItems.put(generatorId, item);
        generatorId++;
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item newItem = allItems.get(item.getId());

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        Long ownerId = item.getOwner().getId();
        List<Item> items = userItems.get(ownerId);

        items.remove(item);
        items.add(newItem);

        allItems.put(item.getId(), newItem);
        return newItem;
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return Optional.ofNullable(allItems.get(itemId));
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return new ArrayList<>(userItems.get(userId));
    }

    @Override
    public List<Item> searchItem(String text) {
        String searchText = text.toLowerCase();
        return userItems.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
}
