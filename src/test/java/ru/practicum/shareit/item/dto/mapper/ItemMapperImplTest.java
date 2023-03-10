package ru.practicum.shareit.item.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemMapperImplTest {
    @InjectMocks
    private ItemMapperImpl itemMapper;
    private Item newItem;
    private ItemDto newItemDto;
    private ItemForRequestDto itemForRequestDto;

    @BeforeEach
    public void beforeEach() {
        newItem = Item.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .owner(null)
                .request(null)
                .build();
        newItemDto = ItemDto.builder()
                .id(1)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(10)
                .build();
    }

    @Test
    public void mapItemToItemDto() {
        ItemDto itemDto = itemMapper.mapItemToItemDto(newItem);
        assertEquals(newItem.getName(), itemDto.getName());
        assertEquals(newItem.getDescription(), itemDto.getDescription());
    }

    @Test
    public void mapItemDtoToItem() {
        Item item = itemMapper.mapItemDtoToItem(newItemDto);
        assertEquals(newItemDto.getName(), item.getName());
    }

    @Test
    public void mapItemToItemPatchDto() {
        ItemPatchDto itemPatchDto = itemMapper.mapItemToItemPatchDto(newItem);
        assertEquals(newItem.getDescription(), itemPatchDto.getDescription());
    }

    @Test
    public void mapItemToItemForRequestDto() {
        itemForRequestDto = itemMapper.mapItemToItemForRequestDto(newItem);
        assertEquals(newItem.getName(), itemForRequestDto.getName());
    }
}