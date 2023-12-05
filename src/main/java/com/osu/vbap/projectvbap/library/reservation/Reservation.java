package com.osu.vbap.projectvbap.library.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"reviews", "reservations", "loans"})
    @NotNull
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"copies", "reviews", "authors"})
    @NotNull
    private Book book;
}