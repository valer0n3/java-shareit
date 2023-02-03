package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDTO;
import ru.practicum.shareit.item.model.Item;

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
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    ItemWithBookingDatesDTO mapItemToItemWithBookingDatesDTO(
            Item item, Booking lastBooking, Booking nextBooking);
}

