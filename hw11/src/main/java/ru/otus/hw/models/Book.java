package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class Book {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Author author;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Genre genre;

    @DBRef
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Comment> comments;

    public Book(String id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
