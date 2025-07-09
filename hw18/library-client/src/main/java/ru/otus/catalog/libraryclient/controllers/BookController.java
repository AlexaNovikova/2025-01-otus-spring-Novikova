package ru.otus.catalog.libraryclient.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.catalog.libraryclient.services.BookService;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDtoWithComments getBookById(@PathVariable("id") long id) {
        return bookService.findBookById(id);
    }

    @PostMapping("/api/v1/books")
    public BookDto addBook(@RequestBody @Valid BookToSaveDto bookToSaveDto) {
        return bookService.create(bookToSaveDto);
    }

    @PutMapping("/api/v1/books")
    public BookDto updateBook(@RequestBody @Valid BookToSaveDto bookToSaveDto) {
        return bookService.update(bookToSaveDto);
    }

    @DeleteMapping("/api/v1/books")
    public void deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
    }
}
