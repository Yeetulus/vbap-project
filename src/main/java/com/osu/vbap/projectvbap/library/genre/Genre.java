package com.osu.vbap.projectvbap.library.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osu.vbap.projectvbap.library.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
    private Set<Book> books;
}