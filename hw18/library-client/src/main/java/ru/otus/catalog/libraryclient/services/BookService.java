package ru.otus.catalog.libraryclient.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;

import java.util.List;

public interface BookService {
    BookDtoWithComments findBookById(long id);

    List<BookDto> findAll();

    BookDto create(BookToSaveDto bookToSaveDto);

    BookDto update(BookToSaveDto bookToSaveDto);

    void deleteById(long id);
}
