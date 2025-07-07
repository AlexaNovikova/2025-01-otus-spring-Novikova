package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.BookDto;
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

    private final static long BOOK_ID = 1L;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentRepository commentRepository;


    @DisplayName(" должен загружать книгу по id со всеми связанными сущностями")
    @WithMockUser(value = "admin", authorities = {"ROLE_ADULT", "ROLE_EDITOR"})
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

    @DisplayName(" должен выбрасывать исключение AccessDenied при обращении п" +
            "ользователя-ребенка к книге 18+")
    @WithMockUser(value = "kid_user", authorities = {"ROLE_USER", "ROLE_KID"})
    @Test
    void shouldThrowAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () -> bookService.findById(4L));
    }

    @DisplayName(" должен загружать все книги с авторами и жанрами для взрослого пользователя")
    @WithMockUser(value = "admin", authorities = {"ROLE_ADULT", "ROLE_EDITOR"})
    @Test
    void shouldFindAllBooks() {
        var actualBooksList = bookService.findAll();
        assertThat(actualBooksList.size()).isNotZero();
        assertThat(actualBooksList.size()).isEqualTo(4);
        var book = actualBooksList.get(0);
        assertDoesNotThrow(book::getAuthor);
        assertDoesNotThrow(book::getGenre);
    }


    @DisplayName(" должен загружать только книги, разрешенные для пользователя-ребенка")
    @WithMockUser(value = "kid_user", authorities = {"ROLE_USER", "ROLE_KID"})
    @Test
    void shouldFindAllBooksAccessedForKids() {
        var actualBooksList = bookService.findAll();
        assertThat(actualBooksList.size()).isNotZero();
        assertThat(actualBooksList.size()).isEqualTo(3);
        var book = actualBooksList.get(0);
        assertDoesNotThrow(book::getAuthor);
        assertDoesNotThrow(book::getGenre);
    }

    @DisplayName(" должен сохранить новую книгу с указанными автором и жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockUser(value = "admin", authorities = {"ROLE_ADULT", "ROLE_EDITOR"})
    @Test
    void shouldSaveNewBookWithSpecifiedAuthorAndGenre() {
        var saved = bookService.save(
                new BookToSaveDto(null, "NewBook", false, 1L, 1L));
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
    }

    @DisplayName(" должен обновить книгу с указанным id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockUser(value = "admin", authorities = {"ROLE_ADULT", "ROLE_EDITOR"})
    @Test
    void shouldUpdateBookWithSpecifiedId() {
        BookDto updated = bookService.save(new BookToSaveDto(
                1L, "UpdatedBook", false, 2L, 2L));
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
    }

    @DisplayName(" должен выбросить исключение EntityNotFoundException " +
            "при указании несуществующего id для автора либо жанра")
    @Test
    void shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto
                        (0L, "NewBook", false, 5L, 1L)));
        assertThrows(EntityNotFoundException.class, () ->
                bookService.save(new BookToSaveDto
                        (0L, "NewBook", false, 1L, 10L)));
    }

    @DisplayName(" должен удалять книгу по id вместе с комментариями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @WithMockUser(value = "admin", authorities = {"ROLE_ADULT", "ROLE_EDITOR"})
    @Test
    void shouldDeleteBookByIdWithAllComments() {
        bookService.deleteById(1L);
        Assertions.assertThat(bookService.findById(1L)).isEmpty();
        Assertions.assertThat(commentRepository.findByBookId(1L))
                .isEqualTo(Collections.EMPTY_LIST);
    }
}
