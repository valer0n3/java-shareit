package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
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

    ItemWithBookingDatesDTO mapItemToItemWithBookingDatesDTO(
            Item item, BookingDto latestBooking, BookingDto nearestBooing);
}

