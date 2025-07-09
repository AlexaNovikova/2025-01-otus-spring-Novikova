package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookDtoWithComments {
    private Long id;

    private String title;

    private boolean adultOnly;

    private AuthorDto author;

    private GenreDto genre;

    private List<CommentDto> comments;
}
