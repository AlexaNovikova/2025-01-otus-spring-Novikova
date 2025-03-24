package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Book> bookList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author author)) return false;
        return getId() == author.getId() && Objects.equals(getFullName(), author.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName());
    }
}
