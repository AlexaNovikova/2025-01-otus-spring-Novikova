package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDtoWithComments> findById(String id);

    List<BookDto> findAll();

    BookDto save(BookToSaveDto bookToSaveDto);

    void deleteById(String id);
}
