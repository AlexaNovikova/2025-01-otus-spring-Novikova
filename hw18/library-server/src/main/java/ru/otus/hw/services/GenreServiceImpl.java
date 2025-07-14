package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.mapper.GenreToDtoConverter;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreToDtoConverter genreToDtoConverter;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository
                .findAll()
                .stream()
                .map(genreToDtoConverter::convert)
                .toList();
    }

    @Override
    @Transactional
    public GenreDto save(GenreDto genreDto) {
        return genreToDtoConverter.convert(
                genreRepository.save(genreToDtoConverter.convertToEntity(genreDto)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        var genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            if (!bookRepository.findByGenre(genre.get()).isEmpty()) {
                throw new DeleteEntityException("Невозможно удалить жанр, " +
                        "в библиотеке есть книги с указанным жанром");
            } else {
                genreRepository.deleteById(id);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenreDto> findById(long id) {
        return genreRepository.findById(id).map(genreToDtoConverter::convert);
    }
}
