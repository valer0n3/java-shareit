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
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public ItemDto addNewItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userID) {
        System.out.println("******** " + userID);
        return itemservice.addNewItem(itemDto, userID);
    }

    @PatchMapping("/{itemId}")
    public ItemPatchDto updateItem(@RequestBody ItemPatchDto itemPatchDto,
                                   @RequestHeader("X-Sharer-User-Id") int userId,
                                   @PathVariable int itemId) {
        return itemservice.updateItem(itemPatchDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return itemservice.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsForOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemservice.getAllItemsForOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemservice.searchItem(text);
    }
}
