package com.osu.vbap.projectvbap.library.copy;

import com.osu.vbap.projectvbap.library.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findAllByBook(Book book);
}
