package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public Book booDtoToBook(BookDto bookDto) {
        var author = authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id %d not found".formatted(bookDto.getAuthorId())));
        var genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Genre with id %d not found".formatted(bookDto.getGenreId())));
        return new Book(bookDto.getId(), bookDto.getTitle(), author, genre);
    }
}
