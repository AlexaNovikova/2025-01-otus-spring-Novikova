package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::bookToStringWithComments)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins newBook a1 g1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        try {
            var savedBook = bookService.save(new BookToSaveDto(
                    null, title, authorId, genreId));
            return bookConverter.bookToString(savedBook);
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    // bupd b2 editedBook a3 g2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        try {
            var savedBook = bookService.save(new BookToSaveDto(
                    id, title, authorId, genreId));
            return bookConverter.bookToString(savedBook);
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    // bdel b1
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
