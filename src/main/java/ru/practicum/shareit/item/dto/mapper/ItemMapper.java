package ru.practicum.shareit.item.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public static ItemDto mapItemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }

    public static Item mapItemDtoToItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest()).build();
    }

    public static ItemPatchDto mapItemToItemPatchDto(Item item) {
        return ItemPatchDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }

    public static Item mapItemPatchDtoToItem(ItemPatchDto itemPatchDto) {
        return Item.builder()
                .name(itemPatchDto.getName())
                .description(itemPatchDto.getDescription())
                .available(itemPatchDto.getAvailable())
                .request(itemPatchDto.getRequest()).build();
    }
}
