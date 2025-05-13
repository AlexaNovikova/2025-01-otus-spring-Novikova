package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.models.Book;

@Mapper(componentModel = "spring",
        uses = {AuthorToDtoConverter.class, GenreToDtoConverter.class, CommentToDtoConverter.class})
public interface BookToDtoConverter {

    BookDto convert(Book book);

    BookDtoWithComments convertToBookDtoWithComments(Book book);

    Book convertToEntity(BookToSaveDto bookToSaveDto);
}
