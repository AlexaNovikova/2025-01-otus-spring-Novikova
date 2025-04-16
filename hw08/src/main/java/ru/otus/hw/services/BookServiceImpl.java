package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentRepository commentRepository;

    @Override
    public Optional<BookDtoWithComments> findById(String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookMapper::modelToDtoWithComments);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::modelToDto)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto save(BookToSaveDto bookToSaveDto) {
        Book book = bookMapper.newBookToSaveDtoToBook(bookToSaveDto);
        List<Comment> comments = null;
        if (bookToSaveDto.getId() != null) {
            comments = commentRepository.findByBookId(bookToSaveDto.getId());
        }
        book.setComments(comments);
        return bookMapper.modelToDto(bookRepository.save(book));
    }
}
