package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;

import java.util.List;

public interface LoanService {
    Loan createLoan(Long userId, Long copyId);
    void returnCopy(Long loanId);
    Loan extendLoan(Long loanId, Long userId, Integer days);
    List<Loan> getAllLoansByUserId(Long id);
    List<Loan> getAllLoansByUser(User user);
    List<Loan> getAllActiveLoansByUser(User user);
    Loan getLoanById(Long id);
}
