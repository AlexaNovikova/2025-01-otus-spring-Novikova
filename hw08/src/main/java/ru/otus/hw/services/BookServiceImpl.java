package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Override
    public Optional<BookDtoWithComments> findById(String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookToDtoConverter::convertToBookDtoWithComments);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookToDtoConverter::convert)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
        commentRepository.deleteAll(commentRepository.findByBookId(id));
    }

    @Override
    public BookDto save(BookToSaveDto bookToSaveDto) {
        var author = authorRepository.findById(bookToSaveDto.getAuthorId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Author with id %s not found".formatted(bookToSaveDto.getAuthorId())));
        var genre = genreRepository.findById(bookToSaveDto.getGenreId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Genre with id %s not found".formatted(bookToSaveDto.getGenreId())));
        Book book = bookToDtoConverter.convertToEntity(bookToSaveDto);
        book.setAuthor(author);
        book.setGenre(genre);
        List<Comment> comments = null;
        if (bookToSaveDto.getId() != null) {
            comments = commentRepository.findByBookId(bookToSaveDto.getId());
        }
        book.setComments(comments);
        return bookToDtoConverter.convert(bookRepository.save(book));
    }
}
