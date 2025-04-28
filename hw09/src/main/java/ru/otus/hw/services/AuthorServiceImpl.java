package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.mapper.AuthorToDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorToDtoConverter authorToDtoConverter;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository
                .findAll()
                .stream()
                .map(authorToDtoConverter::convert)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDto> findById(long id) {
        return authorRepository.findById(id).map(authorToDtoConverter::convert);
    }

    @Transactional
    @Override
    public AuthorDto save(AuthorDto authorDto) {
        return authorToDtoConverter.convert(
                authorRepository.save(authorToDtoConverter.convertToEntity(authorDto)));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var author = authorRepository.findById(id);
        if (author.isPresent()) {
            if (!bookRepository.findByAuthor(author.get()).isEmpty()) {
                throw new DeleteEntityException("Невозможно удалить автора, у которого есть книги");
            }
            authorRepository.deleteById(id);
        }
    }
}
