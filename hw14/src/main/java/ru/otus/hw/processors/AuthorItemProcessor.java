package ru.otus.hw.processors;

import jakarta.validation.constraints.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.mongo.MongoAuthor;

import java.util.UUID;

@Service
public class AuthorItemProcessor implements ItemProcessor<Author, MongoAuthor> {

    @Cacheable(value = "authors", key = "#author.id")
    @Override
    public MongoAuthor process(@NotNull Author author) {
        return new MongoAuthor(
                String.valueOf(UUID.randomUUID()),
                author.getFullName()
        );
    }
}