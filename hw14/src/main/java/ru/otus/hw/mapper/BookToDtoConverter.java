package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.models.mongo.MongoBook;

@Mapper(componentModel = "spring",
        uses = {AuthorToDtoConverter.class, GenreToDtoConverter.class, CommentToDtoConverter.class})
public interface BookToDtoConverter {

    BookDto convert(MongoBook book);

    BookDtoWithComments convertToBookDtoWithComments(MongoBook book);

    MongoBook convertToEntity(BookToSaveDto bookToSaveDto);
}
