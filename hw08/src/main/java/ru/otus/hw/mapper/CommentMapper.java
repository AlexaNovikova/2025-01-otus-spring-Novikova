package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final BookRepository bookRepository;

    public CommentDto modelToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setBookId(comment.getBook().getId());
        return commentDto;
    }

    public Comment dtoToModel(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %s not found".formatted(commentDto.getBookId())));
        comment.setBook(book);
        return comment;
    }
}
