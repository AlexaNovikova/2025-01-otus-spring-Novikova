package ru.otus.catalog.libraryclient.cache;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;


@Component
public class InMemoryCache {

    public List<BookDto> getBooks() {
        return List.of(BookDto.builder().title("New Book")
                .id(1L)
                .adultOnly(false)
                .author(AuthorDto.builder().id(1L).fullName("Author_1").build())
                .genre(GenreDto.builder().id(1L).name("Genre_1").build())
                .build());
    }

    public BookDtoWithComments getBookById(long id) {
        return BookDtoWithComments.builder().title("New Book")
                .id(id)
                .adultOnly(false)
                .author(AuthorDto.builder().id(1L).fullName("Author_1").build())
                .genre(GenreDto.builder().id(1L).name("Genre_1").build())
                .build();
    }

    public BookDto insertBook(BookToSaveDto bookToSaveDto) {
        return BookDto.builder()
                .id(1L)
                .title(bookToSaveDto.getTitle())
                .adultOnly(bookToSaveDto.isAdultOnly())
                .author(AuthorDto.builder().id(bookToSaveDto.getAuthorId()).fullName("").build())
                .genre(GenreDto.builder().id(bookToSaveDto.getGenreId()).name("").build())
                .build();
    }

    public BookDto updateBook(BookToSaveDto bookToSaveDto) {
        return BookDto.builder()
                .id(1L)
                .title(bookToSaveDto.getTitle())
                .adultOnly(bookToSaveDto.isAdultOnly())
                .author(AuthorDto.builder().id(bookToSaveDto.getAuthorId()).fullName("").build())
                .genre(GenreDto.builder().id(bookToSaveDto.getGenreId()).name("").build())
                .build();
    }
}
