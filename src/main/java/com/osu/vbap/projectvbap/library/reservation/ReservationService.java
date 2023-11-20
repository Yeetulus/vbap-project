package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.library.book.Book;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Long bookId, Long userId);
    Reservation saveReservation(Reservation reservation);
    void cancelPotentialReservation(Long bookId, Long userId);
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    List<Reservation> getReservationsByBook(Book book);
    void deleteReservation(Long id);
}
