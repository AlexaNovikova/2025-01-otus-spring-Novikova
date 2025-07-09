package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/books")
public class BookRestController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        var books = bookService.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found!");
        }
        return books;
    }

    @GetMapping("/{id}")
    public BookDtoWithComments getBookById(@PathVariable Long id) {
        var book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        return book.get();
    }

    @PostMapping("/{id}/comments")
    public BookDtoWithComments addComment(@PathVariable Long id,
                                                          @RequestParam String text) {

        commentService.save(new CommentDto(null, text, id));
        var book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        return book.get();
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public BookDtoWithComments deleteComment(@PathVariable Long id,
                                                          @PathVariable Long commentId) {
        commentService.deleteById(commentId);
        var book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        return book.get();
    }

    @PostMapping
    public BookDto addBook(@Valid @RequestBody BookToSaveDto bookToSaveDto) {
        return bookService.save(bookToSaveDto);
    }

    @PutMapping
    public BookDto updateBook(@Valid @RequestBody BookToSaveDto bookToSaveDto) {
        return bookService.save(bookToSaveDto);
    }

    @DeleteMapping
    public void deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
    }
}
