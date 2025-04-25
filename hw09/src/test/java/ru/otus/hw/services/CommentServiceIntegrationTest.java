package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционный тест сервиса по работе с комментариями к книгам")
@SpringBootTest
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName(" должен загружать комментарий по id")
    @Test
    void shouldFindCommentById() {
        var optionalActualComment = commentService.findById(1L);
        assertThat(optionalActualComment).isPresent();
    }

    @DisplayName(" должен возвращать пустой Optional, если комментарий по id не найден ")
    @Test
    void shouldReturnEmptyOptionalWhenCommentIsNotFoundById() {
        var optionalActualComment = commentService.findById(10L);
        assertThat(optionalActualComment).isEmpty();
    }

    @DisplayName(" должен загружать список комментариев по id книги")
    @Test
    void shouldFindCommentListForBookId() {
        var comments = commentService.findByBookId(1L);

        var expectedComments = List.of(
                new CommentDto(1L, "comment_1 to book_1", 1L),
                new CommentDto(2L, "comment_2 to book_1", 1L)
        );
        assertThat(comments.size()).isEqualTo(expectedComments.size());
        assertThat(comments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName(" должен сохранить новый комментарий к указанной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldSaveNewCommentToBook() {
        var savedComment = commentService.save(new CommentDto(0L, "New Comment", 1L));
        var id = savedComment.getId();
        assertThat(id).isNotZero();
        var optionalCommentFromDB = commentService.findById(id);
        assertThat(optionalCommentFromDB).isPresent()
                .get()
                .matches(c->c.getText().equals(savedComment.getText()));
    }

    @DisplayName(" должен удалять комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteCommentById() {
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }
}
