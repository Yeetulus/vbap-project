package com.osu.vbap.projectvbap.library.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"reviews", "reservations", "loans"})
    private User user;
    @ManyToOne
    @JsonIgnoreProperties({"copies", "reviews", "authors"})
    private Book book;
}