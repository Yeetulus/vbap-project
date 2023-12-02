package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.library.book.Book;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Long bookId, Long userId);
    void cancelPotentialReservation(Long bookId, Long userId);
    void cancelReservation(Long bookId, Long userId);
    List<Reservation> getUserReservations(Long userId);
    List<Reservation> getBookReservations(Long bookId);
}
