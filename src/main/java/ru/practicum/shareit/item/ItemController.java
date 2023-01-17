package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemservice;

    @PostMapping
    public ItemDto addNewItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userID) {
        return null;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {
        return null;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return null;
    }

    @GetMapping
    public List<ItemDto> getAllItemsForOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        return null;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return null;
    }
}
