package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                .stream()
                .map(commentMapper::modelToDto)
                .toList();
    }

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(commentMapper::modelToDto);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto save(CommentDto commentDto) {
        Comment comment = commentMapper.dtoToModel(commentDto);
        return commentMapper.modelToDto(commentRepository.save(comment));
    }
}
