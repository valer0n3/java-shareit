package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment mapCommentDtoToComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "commentAuthor.name")
    CommentDto mapCommentToCommentDto(Comment comment);
}
