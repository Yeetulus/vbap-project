package com.osu.vbap.projectvbap.library.loan;

import com.osu.vbap.projectvbap.exception.ItemNotAvailableException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.exception.ItemNotOwnedException;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.copy.BookCopyService;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;
import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notOwnedMessage;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ReservationService reservationService;
    private final UserService userService;
    private final BookCopyService bookCopyService;

    @Value("${library.loanTime}")
    private int defaultLoanTime;
    @Value("${library.defaultExtendTime}")
    private int defaultExtendTime;
    @Override
    public Loan createLoan(Long userId, Long copyId) {

        var bookCopy = bookCopyService.getCopy(copyId);

        if(!bookCopy.getBookCondition().equals(BookCopyCondition.AVAILABLE)) throw new ItemNotAvailableException("Book copy is not available");

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();

        var newLoan = Loan.builder()
                .copy(bookCopy)
                .user(userService.getUser(userId))
                .dateBorrowed(Date.from(today.atStartOfDay(zoneId).toInstant()))
                .scheduledReturnDate(Date.from(today.plusDays(defaultLoanTime).atStartOfDay(zoneId).toInstant()))
                .build();

        reservationService.cancelPotentialReservation(bookCopy.getBook().getId(), userId);
        return loanRepository.save(newLoan);
    }

    @Override
    public void returnCopy(Long loanId) {
        var loan = loanRepository.findById(loanId).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, loanId)));

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        loan.setActualReturnDate(Date.from(today.atStartOfDay(zoneId).toInstant()));
        loan.getCopy().setBookCondition(BookCopyCondition.AVAILABLE);

        loanRepository.save(loan);
    }

    @Override
    public Loan extendLoan(Long loanId, Long userId, Integer days) {
        if(!userId.equals(loanId)) throw new ItemNotOwnedException(String.format(notOwnedMessage, loanId));

        var loan = loanRepository.findById(loanId).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, loanId)));

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        LocalDate scheduledReturnDate = loan.getScheduledReturnDate().toInstant().atZone(zoneId).toLocalDate();

        if (scheduledReturnDate.isAfter(today.plusDays(1))) {
            loan.setScheduledReturnDate(Date.from(scheduledReturnDate.plusDays(days!=null? days : defaultExtendTime).atStartOfDay(zoneId).toInstant()));
            loanRepository.save(loan);
            return loan;
        } else {
            throw new IllegalStateException("Cannot extend loan when the scheduled return date is less than one day from today.");
        }
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