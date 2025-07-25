package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookDto {

    private Long id;

    private String title;

    private boolean adultOnly;

    private AuthorDto author;

    private GenreDto genre;
}
