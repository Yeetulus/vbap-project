package com.osu.vbap.projectvbap.library.copy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"copies", "reviews", "loans"})
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCopyCondition bookCondition = BookCopyCondition.AVAILABLE;

    @OneToMany(mappedBy = "copy")
    @JsonIgnore
    private List<Loan> loans;

}