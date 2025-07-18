package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // ac c1
    @ShellMethod(value = "Find comment by id", key = "ac")
    public String findById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    // acbi b2
    @ShellMethod(value = "Find all comments for book", key = "acbi")
    public String findAllCommentsForBook(String bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //cins newComment b2
    @ShellMethod(value = "Add new comment for book", key = "cins")
    public String addCommentForBook(String commentText, String bookId) {
        try {
            var savedComment = commentService.save(
                    new CommentDto(null, commentText, bookId));
            return commentConverter.commentToString(savedComment);
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
