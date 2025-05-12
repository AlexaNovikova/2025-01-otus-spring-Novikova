package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookToDtoConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookToDtoConverter bookToDtoConverter;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDtoWithComments> findById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookToDtoConverter::convertToBookDtoWithComments);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findBookDtoById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookToDtoConverter::convert);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookToDtoConverter::convert)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public Book save(BookToSaveDto bookToSaveDto) {
        var author = authorRepository.findById(bookToSaveDto.getAuthorId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Author with fullName %s not found".formatted(bookToSaveDto.getAuthorId())));
        var genre = genreRepository.findById(bookToSaveDto.getGenreId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Genre with name %s not found".formatted(bookToSaveDto.getGenreId())));

        Book book = bookToDtoConverter.convertToEntity(bookToSaveDto);
        book.setGenre(genre);
        book.setAuthor(author);
        List<Comment> comments = null;
        if (book.getId() != null) {
            comments = commentRepository.findByBookId(book.getId());
        }
        book.setComments(comments);
        return bookRepository.save(book);
    }
}
