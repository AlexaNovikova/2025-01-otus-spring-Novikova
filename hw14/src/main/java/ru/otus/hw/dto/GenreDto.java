package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private String id;

    @NotBlank(message = "Название жанра не может быть пустым")
    @Size(min = 2, max = 255, message = "Длина названия жанра может быть от 2 до 255 символов")
    private String name;
}
