package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.library.book.BookCopy;
import com.osu.vbap.projectvbap.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookCopy copy;
    @ManyToOne
    private User user;

    @Temporal(TemporalType.DATE)
    private Date dateBorrowed;
    @Temporal(TemporalType.DATE)
    private Date scheduledReturnDate;
    @Temporal(TemporalType.DATE)
    private Date actualReturnDate;

}