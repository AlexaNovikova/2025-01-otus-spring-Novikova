package ru.otus.hw.processors;


import jakarta.validation.constraints.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.UUID;

@Service
public class GenreItemProcessor implements ItemProcessor<Genre, MongoGenre> {

    @Cacheable(value = "genres", key = "#genre.id")
    @Override
    public MongoGenre process(@NotNull Genre genre) {
        return new MongoGenre(
                String.valueOf(UUID.randomUUID()),
                genre.getName()
        );
    }
}