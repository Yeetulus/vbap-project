package com.osu.vbap.projectvbap.library.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.author.Author;
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @ManyToOne
    private Genre genre;

    @NotNull
    private Long pages;

    private Date releaseDate;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties("books")
    private Set<Author> authors;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties({"book"})
    private Set<BookCopy> copies;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Reservation> reservations;
}
