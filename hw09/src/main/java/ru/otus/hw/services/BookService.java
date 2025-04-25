package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDtoWithComments> findById(long id);

    Optional<BookDto> findBookDtoById(long id);

    List<BookDto> findAll();

    Book save(BookToSaveDto bookToSaveDto);

//    Book save(BookDto bookDto);

    void deleteById(long id);
}
