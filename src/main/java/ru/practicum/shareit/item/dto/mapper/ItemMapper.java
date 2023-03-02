package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto mapItemToItemDto(Item item);

    Item mapItemDtoToItem(ItemDto itemDto);

    ItemPatchDto mapItemToItemPatchDto(Item item);

    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    @Mapping(target = "available", source = "itemDto.available")
    ItemWithBookingDatesDto mapItemToItemWithBookingDatesDTO(
            ItemDto itemDto, BookingOwnerDTO lastBooking, BookingOwnerDTO nextBooking, List<CommentDto> comments);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemForRequestDto mapItemToItemForRequestDto(Item item);
}

