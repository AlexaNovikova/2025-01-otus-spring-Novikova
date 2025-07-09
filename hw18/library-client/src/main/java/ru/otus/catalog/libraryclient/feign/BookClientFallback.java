package ru.otus.catalog.libraryclient.feign;

import org.springframework.stereotype.Component;
import ru.otus.catalog.libraryclient.cache.InMemoryCache;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;

import java.util.List;

@Component
public class BookClientFallback implements BookFeignClient {

    private final InMemoryCache abstractCache;

    public BookClientFallback(InMemoryCache abstractCache) {
        this.abstractCache = abstractCache;
    }

    @Override
    public List<BookDto> getAllBooks() {
        return abstractCache.getBooks();
    }

    @Override
    public BookDtoWithComments getBookById(long id) {
        return abstractCache.getBookById(id);
    }

    @Override
    public BookDto insertBook(BookToSaveDto bookToSaveDto) {
        return abstractCache.insertBook(bookToSaveDto);
    }

    @Override
    public BookDto updateBook(BookToSaveDto bookToSaveDto) {
        return abstractCache.updateBook(bookToSaveDto);
    }

    @Override
    public void deleteBook(long id) {

    }
}
