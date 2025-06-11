package ru.otus.hw.processors;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.Comment;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<Comment, MongoComment> {

    private final BookItemProcessor bookItemProcessor;

    @Override
    public MongoComment process(@NotNull Comment comment) {

        MongoBook book = bookItemProcessor.process(comment.getBook());

        return new MongoComment(
                String.valueOf(UUID.randomUUID()),
                comment.getText(),
                book
        );
    }
}