package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentRepository;

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
    private CommentRepository jpaCommentRepository;

    @DisplayName(" должен загружать книгу по id со всеми связанными сущностями")
    @Test
    void shouldFindBookById() {
        var optionalActualBook = bookService.findById(BOOK_ID);
        assertThat(optionalActualBook).isPresent();
        if (optionalActualBook.isPresent()) {
            var book = optionalActualBook.get();
            assertThat(book.getTitle()).isEqualTo("BookTitle_1");
            assertDoesNotThrow(book::getAuthor);
            assertDoesNotThrow(book::getGenre);
            assertDoesNotThrow(book::getComments);
        }
    }

    @DisplayName(" должен загружать все книги с авторами и жанрами")
    @Test
    void shouldFindAllBooks() {
        var actualBooksList = bookService.findAll();
        assertThat(actualBooksList.size()).isNotZero();
        var book = actualBooksList.get(0);
        assertDoesNotThrow(book::getAuthor);
        assertDoesNotThrow(book::getGenre);
    }

    @DisplayName(" должен сохранить новую книгу с указанными автором и жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldSaveNewBookWithSpecifiedAuthorAndGenre() {
        var saved = bookService.save(
                new BookToSaveDto(0L, "NewBook", 1L, 1L));
        var id = saved.getId();
        assertThat(id).isNotZero();
        var optionalBookFromDB = bookService.findById(id);
        assertThat(optionalBookFromDB).isPresent()
                .get()
                .matches(b -> b.getTitle().equals(saved.getTitle()))
                .matches(b -> b.getAuthor().getId().equals(saved.getAuthor().getId()))
                .matches(b -> b.getGenre().getId().equals(saved.getGenre().getId()));
        assertDoesNotThrow(saved::getAuthor);
        assertDoesNotThrow(saved::getGenre);
        assertDoesNotThrow(saved::getComments);
    }

    @DisplayName(" должен обновить книгу с указанным id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateBookWithSpecifiedId() {
        Book updated = bookService.save(new BookToSaveDto(
                1L, "UpdatedBook", 2L, 2L));
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getTitle()).isEqualTo("UpdatedBook");
        var updatedFromDB = bookService.findById(1L);
        assertThat(updatedFromDB).isPresent()
                .get()
                .matches(b -> b.getTitle().equals(updated.getTitle()))
                .matches(b -> b.getAuthor().getId().equals(updated.getAuthor().getId()))
                .matches(b -> b.getGenre().getId().equals(updated.getGenre().getId()));
        assertDoesNotThrow(updated::getAuthor);
        assertDoesNotThrow(updated::getGenre);
        assertDoesNotThrow(updated::getComments);
    }

    @DisplayName(" должен выбросить исключение EntityNotFoundException " +
            "при указании несуществующего id для автора либо жанра")
    @Test
    void shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto
                        (0L, "NewBook", 5L, 1L)));
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto
                        (0L, "NewBook", 1L, 10L)));
    }

    @DisplayName(" должен удалять книгу по id вместе с комментариями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteBookByIdWithAllComments() {
        bookService.deleteById(1L);
        Assertions.assertThat(bookService.findById(1L)).isEmpty();
        Assertions.assertThat(jpaCommentRepository.findByBookId(1L))
                .isEqualTo(Collections.EMPTY_LIST);
    }
}
