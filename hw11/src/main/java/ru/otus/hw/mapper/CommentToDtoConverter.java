package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring", uses = BookToDtoConverter.class)
public interface CommentToDtoConverter {

    @Mapping(target = "bookId", source = "book.id")
    CommentDto convert(Comment comment);

    Comment convertToEntity(CommentDto commentDto);
}
