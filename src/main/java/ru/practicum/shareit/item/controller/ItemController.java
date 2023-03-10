package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.variables.Variables.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemservice;

    @PostMapping
    public ItemDto addNewItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(X_SHARER_USER_ID) int userID) {
        return itemservice.addNewItem(itemDto, userID);
    }

    @PatchMapping("/{itemId}")
    public ItemPatchDto updateItem(@RequestBody ItemPatchDto itemPatchDto,
                                   @RequestHeader(X_SHARER_USER_ID) int userId,
                                   @PathVariable int itemId) {
        return itemservice.updateItem(itemPatchDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDatesDto getItemById(@PathVariable int itemId,
                                               @RequestHeader(X_SHARER_USER_ID) int userId) {
        return itemservice.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemWithBookingDatesDto> getAllItemsForOwner(@RequestHeader(X_SHARER_USER_ID) int userId) {
        return itemservice.getAllItemsForOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemservice.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto,
                                 @PathVariable int itemId,
                                 @RequestHeader(X_SHARER_USER_ID) int userId) {
        return itemservice.addComment(commentDto, itemId, userId);
    }
}
