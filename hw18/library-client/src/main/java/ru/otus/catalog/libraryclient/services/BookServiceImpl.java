package ru.otus.catalog.libraryclient.services;

import org.springframework.stereotype.Service;
import ru.otus.catalog.libraryclient.feign.BookFeignClient;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookFeignClient bookFeignClient;

    public BookServiceImpl(BookFeignClient bookFeignClient) {
        this.bookFeignClient = bookFeignClient;
    }

    @Override
    public BookDtoWithComments findBookById(long id) {
        return bookFeignClient.getBookById(id);
    }

    @Override
    public List<BookDto> findAll() {
        return bookFeignClient.getAllBooks();
    }

    @Override
    public BookDto create(BookToSaveDto bookToSaveDto) {
        return bookFeignClient.insertBook(bookToSaveDto);
    }

    @Override
    public BookDto update(BookToSaveDto bookToSaveDto) {
        return bookFeignClient.updateBook(bookToSaveDto);
    }

    @Override
    public void deleteById(long id) {
        bookFeignClient.deleteBook(id);
    }
}
