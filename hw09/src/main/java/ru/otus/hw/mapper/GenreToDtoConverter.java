package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface GenreToDtoConverter {

    GenreDto convert (Genre genre);

    Genre convertToEntity(GenreDto genreDto);
}
