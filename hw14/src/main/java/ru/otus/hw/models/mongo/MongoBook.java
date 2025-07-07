package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
@Builder(toBuilder = true)
public class MongoBook {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    private MongoAuthor author;

    private MongoGenre genre;

    @DBRef
    private List<MongoComment> comments;

    public MongoBook(String id, String title, MongoAuthor author, MongoGenre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
