package ru.practicum.shareit.request.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    Request mapNewRequestDtoToRequest(NewRequestDto requestDto);

    RequestDto mapRequestToRequestDto(Request request);
}
