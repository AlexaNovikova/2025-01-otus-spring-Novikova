package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentToDtoConverter commentToDtoConverter;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository
                .findByBookId(bookId)
                .stream()
                .map(commentToDtoConverter::convert)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        var commentOptional = commentRepository.findById(id);
        return commentOptional.map(commentToDtoConverter::convert);
    }

    @Override
    @Transactional
    @PreAuthorize("canDelete(#id, T(ru.otus.hw.models.Comment)) " +
            "or hasRole('ADMIN')")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment save(CommentDto commentDto) {
        var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Book with id %d not found".formatted(commentDto.getBookId())));
        Comment comment = commentToDtoConverter.convertToEntity(commentDto);
        comment.setBook(book);
        Comment saved = commentRepository.save(comment);
        aclServiceWrapperService.createPermission(saved, BasePermission.DELETE);
        return saved;
    }
}
