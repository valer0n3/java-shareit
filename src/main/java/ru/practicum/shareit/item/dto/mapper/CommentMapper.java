package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment mapCommentDtoToComment(CommentDto commentDto);

    CommentDto mapCommentToCommentDto(Comment comment);
}
