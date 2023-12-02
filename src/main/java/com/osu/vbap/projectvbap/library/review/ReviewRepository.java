package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByUser(User user);
    List<Review> findAllByBook(Book book);
    List<Review> findAllByBook_Id(Long bookId);
    boolean existsByUserAndBook(User user, Book book);
}
