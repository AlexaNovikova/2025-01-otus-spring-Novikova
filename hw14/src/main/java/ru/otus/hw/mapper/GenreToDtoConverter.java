package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.mongo.MongoGenre;

@Mapper(componentModel = "spring")
public interface GenreToDtoConverter {

    GenreDto convert (MongoGenre genre);

    MongoGenre convertToEntity(GenreDto genreDto);
}
