package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;

    private String text;

    private Long bookId;
}
