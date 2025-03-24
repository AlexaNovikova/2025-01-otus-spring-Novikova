package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaCommentRepositoryTest;


import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Интеграционный тест сервиса по работе с книгами")
@SpringBootTest
public class BookServiceIntegrationTest {

    private final static long BOOK_ID = 1L;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @DisplayName(" должен загружать книгу по id со всеми связанными сущностями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindBookById() {
        var optionalActualBook = bookService.findById(BOOK_ID);
        assertThat(optionalActualBook).isPresent();
        var book = optionalActualBook.get();
        assertDoesNotThrow(book::getAuthor);
        assertDoesNotThrow(book::getGenre);
        assertDoesNotThrow(book::getComments);
    }

    @DisplayName(" должен загружать все книги со всеми связанными сущностями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBooks() {
        var actualBooksList = bookService.findAll();
        assertThat(actualBooksList.size()).isNotZero();
        var book = actualBooksList.get(0);
        assertDoesNotThrow(book::getAuthor);
        assertDoesNotThrow(book::getGenre);
        assertDoesNotThrow(book::getComments);
    }

    @DisplayName(" должен сохранить новую книгу с указанными автором и жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldSaveNewBookWithSpecifiedAuthorAndGenre() {
        Book saved = bookService.save(0, "NewBook", 1, 1);
        long id = saved.getId();
        assertThat(id).isNotZero();
        var optionalBookFromDB = bookService.findById(id);
        assertThat(optionalBookFromDB).isPresent()
                .get().isEqualTo(saved);
        assertDoesNotThrow(saved::getAuthor);
        assertDoesNotThrow(saved::getGenre);
        assertDoesNotThrow(saved::getComments);
    }

    @DisplayName(" должен обновить книгу с указанным id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldUpdateBookWithSpecifiedId() {
        Book updated = bookService.save(1, "UpdatedBook", 2, 2);
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getTitle()).isEqualTo("UpdatedBook");
        var updatedFromDB = bookService.findById(1L);
        assertThat(updatedFromDB).isPresent()
                .get().isEqualTo(updated);
        assertDoesNotThrow(updated::getAuthor);
        assertDoesNotThrow(updated::getGenre);
        assertDoesNotThrow(updated::getComments);
    }

    @DisplayName(" должен выбросить исключение EntityNotFoundException " +
            "при указании несуществующего id для автора либо жанра")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(0, "NewBook", 5, 1));
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(0, "NewBook", 1, 11));
    }

    @DisplayName(" должен удалять книгу по id вместе с комментариями")
    @Test
    void shouldDeleteBookByIdWithAllComments() {
        bookService.deleteById(1L);
        Assertions.assertThat(bookService.findById(1L)).isEmpty();
        Assertions.assertThat(jpaCommentRepository.findByBookId(1L))
                .isEqualTo(Collections.EMPTY_LIST);
    }
}
