package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.library.loan.Loan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCopyCondition bookCondition;

    @OneToMany(mappedBy = "copy")
    private List<Loan> loans;

}