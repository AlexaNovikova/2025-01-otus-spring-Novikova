package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAll();

    GenreDto save(GenreDto genreDto);

    void deleteById(long id);

    Optional<GenreDto> findById(long id);
}
