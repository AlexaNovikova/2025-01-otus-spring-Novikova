package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreToDtoConverter;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final MongoGenreRepository genreRepository;

    private final GenreToDtoConverter genreToDtoConverter;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreToDtoConverter::convert)
                .toList();
    }
}
