package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.author.Author;
import com.osu.vbap.projectvbap.library.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Book {
    @Id
    Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    private Genre genre;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    Set<Author> authors;

    @OneToMany(mappedBy = "book")
    private Set<BookCopy> copies;

    @OneToMany(mappedBy = "book")
    private Set<Review> reviews;
}
