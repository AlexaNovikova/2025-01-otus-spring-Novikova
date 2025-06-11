package ru.otus.hw.processors;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<Book, MongoBook> {

    private final AuthorItemProcessor authorItemProcessor;

    private final GenreItemProcessor genreItemProcessor;

    @Cacheable(value = "books", key = "#book.id")
    @Override
    public MongoBook process(@NotNull Book book) {
        MongoAuthor author = authorItemProcessor.process(book.getAuthor());
        MongoGenre genre = genreItemProcessor.process(book.getGenre());

        return new MongoBook(
                String.valueOf(UUID.randomUUID()),
                book.getTitle(),
                author,
                genre
        );
    }
}
