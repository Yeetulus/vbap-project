package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByBook(Book book);
    Optional<Reservation> findByBook_IdAndUser_Id(Long bookId, Long userId);
}
