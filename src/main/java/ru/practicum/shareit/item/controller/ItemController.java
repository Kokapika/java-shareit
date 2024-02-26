package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemServiceImpl itemServiceDtoImpl;

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_HEADER) Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("POST-запрос на добавление пользователем с id = " + userId + " предмета: " + itemDto.toString());
        return itemServiceDtoImpl.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_HEADER) Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {
        log.info("PATCH-запрос на обновление предмета с id = " + itemId + " пользователем с id = " + userId);
        return itemServiceDtoImpl.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(USER_HEADER) Long userId,
                            @PathVariable Long itemId) {
        log.info("GET-запрос на получение предмета с id = " + itemId + " пользователем с id = " + userId);
        return itemServiceDtoImpl.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("GET-запрос на получение предметов пользователя с id = " + userId);
        return itemServiceDtoImpl.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                     @RequestParam String text) {
        log.info("GET-запрос на поиск предметов");
        return itemServiceDtoImpl.searchItem(userId, text);
    }
}
