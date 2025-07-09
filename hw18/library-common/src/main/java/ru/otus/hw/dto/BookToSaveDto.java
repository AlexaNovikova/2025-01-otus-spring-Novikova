package ru.otus.hw.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookToSaveDto {

    private Long id;

    @NotBlank(message = "Название книги не может быть пустым")
    @Size(min = 2, max = 255, message = "Длина названия книги может быть от 2 до 255 символов")
    private String title;

    @NotNull(message = "Наличие/отсутствие возрастных ограничений должно быть указано")
    private boolean adultOnly;

    @NotNull(message = "Автор книги должен быть указан")
    @Min(value = 1, message = "Автор книги должен быть указан")
    private Long authorId;

    @NotNull(message = "Жанр книги должен быть указан")
    @Min(value = 1, message = "Жанр книги должен быть указан")
    private Long genreId;

    public BookToSaveDto(BookDtoWithComments bookDto) {
        this.id = bookDto.getId();
        this.title = bookDto.getTitle();
        this.authorId = bookDto.getAuthor().getId();
        this.genreId = bookDto.getGenre().getId();
    }
}
