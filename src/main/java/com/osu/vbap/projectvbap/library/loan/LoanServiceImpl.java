package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import com.osu.vbap.projectvbap.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ReservationService reservationService;

    @Value("${library.loanTime}")
    private Long defaultLoanTime;
    @Override
    public Loan createLoan(User user, BookCopy bookCopy) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();

        var newLoan = Loan.builder()
                .copy(bookCopy)
                .user(user)
                .dateBorrowed(Date.from(today.atStartOfDay(zoneId).toInstant()))
                .scheduledReturnDate(Date.from(today.plusDays(defaultLoanTime).atStartOfDay(zoneId).toInstant()))
                .build();

        reservationService.cancelPotentialReservation(bookCopy.getBook().getId(), user.getId());
        return loanRepository.save(newLoan);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    @Override
    public List<Loan> getAllLoansByBook(Book book) {
        return loanRepository.findAllByCopy_Book(book);
    }

    @Override
    public List<Loan> getAllLoansByUser(User user) {
        return loanRepository.findAllByUser(user);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }
    @Override
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
}