package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.exception.ItemAlreadyReservedException;
import com.osu.vbap.projectvbap.exception.ItemNotAvailableException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessage;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final UserService userService;

    @Override
    public Reservation createReservation(Long bookId, Long userId){
        var bookToReserve = bookService.getBook(bookId);
        var user = userService.getUser(userId);

        if(reservationRepository.existsByUserAndBook(user, bookToReserve))
                throw new ItemAlreadyReservedException(String.format("Book %s is already reserved", bookId));


        int availableCount = bookService.getAvailableCount(bookToReserve);
        if(availableCount < 1 ) throw new ItemNotAvailableException(String.format("Book with ID %s is not available", bookId));

        var reservation = Reservation.builder()
                .book(bookToReserve)
                .user(user)
                .build();

        return reservationRepository.save(reservation);
    }

    @Override
    public void cancelPotentialReservation(Long bookId, Long userId) {

        Optional<Reservation> reservation = reservationRepository.findByBook_IdAndUser_Id(bookId, userId);
        if(reservation.isEmpty()) return;

        reservationRepository.delete(reservation.get());
        logger.info(String.format("Canceled reservation %s", reservation.get().getId()));
    }

    @Override
    public void cancelReservation(Long bookId, Long userId) {
        var reservation = reservationRepository.findByBook_IdAndUser_Id(bookId, userId).orElseThrow(() ->
                new ItemNotFoundException(notFoundMessage));

        reservationRepository.delete(reservation);
    }

    @Override
    public List<Reservation> getUserReservations(Long userId) {
        var user = userService.getUser(userId);
        return reservationRepository.findAllByUser(user);
    }

    @Override
    public List<Reservation> getBookReservations(Long bookId) {
        var book = bookService.getBook(bookId);
        return reservationRepository.findAllByBook(book);
    }
}