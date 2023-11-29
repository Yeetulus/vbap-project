package com.osu.vbap.projectvbap.library.copy;

import com.osu.vbap.projectvbap.library.book.Book;

import java.util.List;

public interface BookCopyService {

    BookCopy createCopy(Long bookId);
    BookCopy updateCopy(Long copyId, BookCopyCondition condition);
    void deleteCopy(Long copyId);
    BookCopy getCopy(Long copyId);
    List<BookCopy> getCopiesByBook(Book book);
}
