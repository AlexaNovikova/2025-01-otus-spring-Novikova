package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookToDtoConverter;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final MongoBookRepository bookRepository;

    private final BookToDtoConverter bookToDtoConverter;

    private final MongoAuthorRepository authorRepository;

    private final MongoGenreRepository genreRepository;

    private final MongoCommentRepository commentRepository;

    @Override
    public Optional<BookDtoWithComments> findById(String id) {
        Optional<MongoBook> bookOptional = bookRepository.findById(id);
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
        var author = authorRepository.findById(String.valueOf(bookToSaveDto.getAuthorId()))
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Author with id %s not found".formatted(bookToSaveDto.getAuthorId())));
        var genre = genreRepository.findById(String.valueOf(bookToSaveDto.getGenreId()))
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Genre with id %s not found".formatted(bookToSaveDto.getGenreId())));
        MongoBook book = bookToDtoConverter.convertToEntity(bookToSaveDto);
        book.setAuthor(author);
        book.setGenre(genre);
        List<MongoComment> comments = null;
        if (bookToSaveDto.getId() != null) {
            comments = commentRepository.findByBookId
                    (String.valueOf(bookToSaveDto.getId()));
        }
        book.setComments(comments);
        return bookToDtoConverter.convert(bookRepository.save(book));
    }
}
