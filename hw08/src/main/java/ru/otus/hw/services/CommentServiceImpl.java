package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentToDtoConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentToDtoConverter commentToDtoConverter;

    @Override
    public List<CommentDto> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                .stream()
                .map(commentToDtoConverter::convert)
                .toList();
    }

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(commentToDtoConverter::convert);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto save(CommentDto commentDto) {
        var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %s not found".formatted(commentDto.getBookId())));
        Comment comment = commentToDtoConverter.convertToEntity(commentDto);
        comment.setBook(book);
        return commentToDtoConverter.convert(commentRepository.save(comment));
    }
}
