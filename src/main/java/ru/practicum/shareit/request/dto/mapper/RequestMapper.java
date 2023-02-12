package ru.practicum.shareit.request.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestAllOtherDTO;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    Request mapNewRequestDtoToRequest(NewRequestDto requestDto);

    RequestDto mapRequestToRequestDto(Request request);

    RequestDto mapRequestToRequestDto(Page<Request> request);

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "created", source = "request.created")
    @Mapping(target = "itemDtoList", source = "items")
    RequestAllOtherDTO mapRequestToRequestAllOtherDto(Request request, List<ItemDto> items);

    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "created", source = "request.created")
    @Mapping(target = "items", source = "items")
    RequestGetAllDto mapRequestToRequestGetAllDto(Request request, List<ItemForRequestDto> items);
}
