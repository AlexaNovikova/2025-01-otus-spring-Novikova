package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return getId() == comment.getId() && Objects.equals(getText(), comment.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText());
    }
}
