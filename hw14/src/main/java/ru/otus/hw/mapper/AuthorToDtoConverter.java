package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.mongo.MongoAuthor;

@Mapper(componentModel = "spring")
public interface AuthorToDtoConverter {

    AuthorDto convert(MongoAuthor author);

    MongoAuthor convertToEntity(AuthorDto authorDto);
}
