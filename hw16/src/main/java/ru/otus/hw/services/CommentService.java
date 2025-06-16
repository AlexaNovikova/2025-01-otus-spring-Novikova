package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findByBookId(long bookId);

    Optional<CommentDto> findById(long id);

    void deleteById(long id);

    Comment save(CommentDto commentDto);
}