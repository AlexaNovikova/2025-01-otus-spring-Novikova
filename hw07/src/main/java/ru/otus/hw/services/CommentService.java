package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findByBookId(long bookId);

    Optional<Comment> findById(long id);

    void deleteById(long id);

    Comment save(long id, String commentText, long bookId);
}