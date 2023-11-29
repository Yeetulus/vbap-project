package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findAllByCopy_Book(Book book);
    List<Loan> findAllByUser(User user);

    Optional<Loan> findByCopy_Id(Long id);
}
