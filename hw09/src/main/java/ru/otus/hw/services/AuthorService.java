package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Map;

public interface AuthorService {
    List<AuthorDto> findAll();
}
