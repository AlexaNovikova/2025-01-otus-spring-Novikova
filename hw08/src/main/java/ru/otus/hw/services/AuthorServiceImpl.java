package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorToDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorToDtoConverter authorToDtoConverter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(authorToDtoConverter::convert).toList();
    }
}
