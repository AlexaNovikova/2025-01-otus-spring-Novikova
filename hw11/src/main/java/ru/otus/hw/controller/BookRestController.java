package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookToDtoConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/books")
public class BookRestController {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookToDtoConverter bookToDtoConverter;

    @GetMapping
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .map(bookToDtoConverter::convert);
    }

    @GetMapping("/{id}")
    public Mono<BookDtoWithComments> getBookById(@PathVariable String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Book with id " + id + " not found")))
                .map(bookToDtoConverter::convertToBookDtoWithComments);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDtoWithComments> addComment(@PathVariable String id,
                                                @RequestParam String text) {

        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error
                        (new EntityNotFoundException("Book with id " + id + " not found")))
                .flatMap(book -> commentRepository
                        .save(new Comment(String.valueOf(UUID.randomUUID()), text, book))
                        .zipWith(bookRepository.findById(id))
                        .flatMap(tuple -> saveBookWithNewComment(tuple.getT2(), tuple.getT1()))
                        .map(bookToDtoConverter::convertToBookDtoWithComments));
    }

    private Mono<Book> saveBookWithNewComment(Book book, Comment comment) {
        if (book.getComments() != null) {
            book.getComments().add(comment);
        } else {
            book.setComments(List.of(comment));
        }
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<BookDtoWithComments> deleteComment(@PathVariable String id,
                                                   @PathVariable String commentId) {
        return commentRepository.deleteById(commentId)
                .then(bookRepository
                        .findById(id)
                        .map(bookToDtoConverter::convertToBookDtoWithComments)
                        .switchIfEmpty(Mono.error
                                (new EntityNotFoundException("Book with id " + id + " not found"))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> addBook(@Valid @RequestBody BookToSaveDto bookToSaveDto) {
        return genreRepository.findById(bookToSaveDto.getGenreId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException
                        ("Genre with name %s not found"
                                .formatted(bookToSaveDto.getGenreId()))))
                .zipWith(authorRepository.findById(bookToSaveDto.getAuthorId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException
                                ("Author with fullName %s not found"
                                        .formatted(bookToSaveDto.getAuthorId())))))
                .flatMap(tuple -> saveNewBook(tuple.getT1(), tuple.getT2(), bookToSaveDto)
                        .map(bookToDtoConverter::convert));
    }

    private Mono<Book> saveNewBook(Genre genre, Author author, BookToSaveDto bookToSaveDto) {
        var newBook = bookToDtoConverter.convertToEntity(bookToSaveDto);
        newBook.setId(String.valueOf(UUID.randomUUID()));
        newBook.setAuthor(author);
        newBook.setGenre(genre);
        return bookRepository.save(newBook);
    }

    @PutMapping
    public Mono<BookDto> updateBook(@Valid @RequestBody BookToSaveDto bookToSaveDto) {
        return genreRepository.findById(bookToSaveDto.getGenreId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException
                        ("Genre with name %s not found"
                                .formatted(bookToSaveDto.getGenreId()))))
                .zipWith(authorRepository.findById(bookToSaveDto.getAuthorId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException
                                ("Author with fullName %s not found"
                                        .formatted(bookToSaveDto.getAuthorId())))))
                .flatMap(tuple -> updateBookEntity(bookToSaveDto, tuple)
                        .map(bookToDtoConverter::convert));
    }

    private Mono<Book> updateBookEntity(BookToSaveDto bookToSaveDto, Tuple2<Genre, Author> entities) {
        return bookRepository.findById(bookToSaveDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found"
                        .formatted(bookToSaveDto.getId()))))
                .map(book -> {
                    book.setGenre(entities.getT1());
                    book.setAuthor(entities.getT2());
                    book.setTitle(bookToSaveDto.getTitle());
                    return book;
                });
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@RequestParam("id") String id) {
        return bookRepository.findById(id)
                .zipWith(commentRepository.findByBookId(id).collectList())
                .flatMap(tuple -> commentRepository.deleteAll(tuple.getT2())
                        .then(bookRepository.delete(tuple.getT1())));
    }
}
