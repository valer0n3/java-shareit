package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDTO;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto mapItemToItemDto(Item item);

    Item mapItemDtoToItem(ItemDto itemDto);

    ItemPatchDto mapItemToItemPatchDto(Item item);

    Item mapItemPatchDtoToItem(ItemPatchDto itemPatchDto);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    ItemWithBookingDatesDTO mapItemToItemWithBookingDatesDTO(
            Item item, BookingOwnerDTO lastBooking, BookingOwnerDTO nextBooking, List<CommentDto> comments);
}

