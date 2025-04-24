package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findByBookId(String bookId);

    Optional<CommentDto> findById(String id);

    void deleteById(String id);

    CommentDto save(CommentDto commentDto);
}