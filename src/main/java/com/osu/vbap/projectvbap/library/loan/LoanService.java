package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.user.User;

import java.util.List;

public interface LoanService {
    Loan createLoan(String userEmail, Long copyId);
    void returnCopy(Long loanId);
    Loan extendLoan(Long loanId, Long userId, Integer days);
    List<Loan> getAllLoansByUserEmail(String userEmail);
    List<Loan> getAllLoansByUser(User user);
    List<Loan> getAllActiveLoansByUser(User user);
    List<Loan> getAllActiveLoansByUser(String userEmail);
    Loan getLoanById(Long id);
}
