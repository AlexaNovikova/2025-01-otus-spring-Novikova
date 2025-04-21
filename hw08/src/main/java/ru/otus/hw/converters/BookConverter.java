package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToStringWithComments(BookDtoWithComments bookDtoWithComments) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s], comments: [%s]".formatted(
                bookDtoWithComments.getId(),
                bookDtoWithComments.getTitle(),
                authorConverter.authorToString(bookDtoWithComments.getAuthor()),
                genreConverter.genreToString(bookDtoWithComments.getGenre()),
                bookDtoWithComments.getComments() != null ?
                        bookDtoWithComments.getComments().stream().map(commentConverter::commentToString)
                                .collect(Collectors.joining("; ")) : "");
    }

    public String bookToString(BookDto bookDto) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.authorToString(bookDto.getAuthor()),
                genreConverter.genreToString(bookDto.getGenre()));
    }
}
