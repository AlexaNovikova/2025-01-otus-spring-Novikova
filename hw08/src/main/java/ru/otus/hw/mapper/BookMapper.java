package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    private final CommentMapper commentMapper;

    public Book newBookToSaveDtoToBook(BookToSaveDto bookToSaveDto) {
        var author = authorRepository.findById(bookToSaveDto.getAuthorId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Author with id %s not found".formatted(bookToSaveDto.getAuthorId())));
        var genre = genreRepository.findById(bookToSaveDto.getGenreId())
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Genre with id %s not found".formatted(bookToSaveDto.getGenreId())));
        return new Book(bookToSaveDto.getId(), bookToSaveDto.getTitle(), author, genre);
    }

    public BookDto modelToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(authorMapper.modelToDto(book.getAuthor()));
        bookDto.setGenreDto(genreMapper.modelToDto(book.getGenre()));
        return bookDto;
    }

    public BookDtoWithComments modelToDtoWithComments(Book book) {
        BookDtoWithComments bookDtoWithComments = new BookDtoWithComments();
        bookDtoWithComments.setId(book.getId());
        bookDtoWithComments.setTitle(book.getTitle());
        bookDtoWithComments.setAuthorDto(authorMapper.modelToDto(book.getAuthor()));
        bookDtoWithComments.setGenreDto(genreMapper.modelToDto(book.getGenre()));
        if (book.getComments() != null) {
            bookDtoWithComments.setCommentDtos(book.getComments()
                    .stream()
                    .map(commentMapper::modelToDto)
                    .toList());
        } else {
            bookDtoWithComments.setCommentDtos(List.of());
        }
        return bookDtoWithComments;
    }
}
