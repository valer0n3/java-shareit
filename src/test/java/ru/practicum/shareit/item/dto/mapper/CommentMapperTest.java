package ru.practicum.shareit.item.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    @InjectMocks
    private CommentMapperImpl commentMapper;
    private Comment newComment;
    private CommentDto newCommentDto;

    @BeforeEach
    public void beforeEachCreateRequests() {
        newComment = Comment.builder()
                .id(1)
                .text("commentTest")
                .build();
        newCommentDto = CommentDto.builder()
                .id(1)
                .text("commentTest")
                .build();
    }

    @Test
    void mapCommentDtoToComment() {
        Comment comment = commentMapper.mapCommentDtoToComment(newCommentDto);
        assertEquals(newCommentDto.getText(), comment.getText());
    }

    @Test
    void mapCommentToCommentDto() {
        CommentDto commentDto = commentMapper.mapCommentToCommentDto(newComment);
        assertEquals(newComment.getText(), commentDto.getText());
    }
}