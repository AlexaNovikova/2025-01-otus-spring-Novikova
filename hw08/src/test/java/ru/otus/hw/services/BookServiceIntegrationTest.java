package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Интеграционный тест сервиса по работе с книгами")
@SpringBootTest
public class BookServiceIntegrationTest {

    private final static String BOOK_ID = "b1";

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName(" должен загружать книгу по id c автором, жанром и комментариями")
    @Test
    void shouldFindBookById() {
        var optionalActualBook = bookService.findById(BOOK_ID);
        assertThat(optionalActualBook).isPresent();
        if (optionalActualBook.isPresent()) {
            var book = optionalActualBook.get();
            assertThat(book.getTitle()).isEqualTo("BookTitle_1");
            assertThat(book)
                    .matches(b -> b.getAuthorDto().getFullName().equals("Author_1"))
                    .matches(b -> b.getGenreDto().getName().equals("Genre_1"))
                    .matches(b -> b.getCommentDtos().size() == 3);
        }
    }

    @DisplayName(" должен загружать все книги с авторами и жанрами")
    @Test
    void shouldFindAllBooks() {
        var actualBooksList = bookService.findAll();
        assertThat(actualBooksList.size()).isNotZero();
        var book = actualBooksList.get(0);
        assertThat(book)
                .matches(b -> b.getAuthorDto().getFullName().equals("Author_1"))
                .matches(b -> b.getGenreDto().getName().equals("Genre_1"));
    }

    @DisplayName(" должен сохранить новую книгу с указанными автором и жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldSaveNewBookWithSpecifiedAuthorAndGenre() {
        var saved = bookService.save(new BookToSaveDto(null, "NewBook", "a1", "g1"));
        var id = saved.getId();
        assertThat(id).isNotEmpty();
        var optionalBookFromDB = bookService.findById(id);
        assertThat(optionalBookFromDB).isPresent();
        var bookFromDb = optionalBookFromDB.get();
        assertThat(bookFromDb)
                .matches(b -> b.getTitle().equals("NewBook"))
                .matches(b -> b.getAuthorDto().getFullName().equals("Author_1"))
                .matches(b -> b.getGenreDto().getName().equals("Genre_1"))
                .matches(b -> b.getCommentDtos().isEmpty());
    }

    @DisplayName(" должен обновить книгу с указанным id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateBookWithSpecifiedId() {
        var updated = bookService.save(new BookToSaveDto("b1", "UpdatedBook", "a2", "g2"));
        assertThat(updated.getId()).isEqualTo("b1");
        assertThat(updated.getTitle()).isEqualTo("UpdatedBook");
        var updatedFromDB = bookService.findById("b1");
        assertThat(updatedFromDB).isPresent();
        var bookFromDb = updatedFromDB.get();
        assertThat(bookFromDb)
                .matches(b -> b.getTitle().equals("UpdatedBook"))
                .matches(b -> b.getAuthorDto().getFullName().equals("Author_2"))
                .matches(b -> b.getGenreDto().getName().equals("Genre_2"))
                .matches(b -> b.getCommentDtos().size() == 3);
    }

    @DisplayName(" должен выбросить исключение EntityNotFoundException " +
            "при указании несуществующего id для автора либо жанра")
    @Test
    void shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto(null, "NewBook", "a5", "g1")));
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto(null, "NewBook", "a1", "g11")));
    }

    @DisplayName(" должен удалять книгу по id вместе с комментариями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteBookByIdWithAllComments() {
        bookService.deleteById("b1");
        Assertions.assertThat(bookService.findById("b1")).isEmpty();
        Assertions.assertThat(commentRepository.findByBookId("b1"))
                .isEqualTo(Collections.EMPTY_LIST);
    }
}
