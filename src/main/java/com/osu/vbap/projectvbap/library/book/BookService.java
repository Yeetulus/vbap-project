package com.osu.vbap.projectvbap.library.book;

import java.util.List;

public interface BookService {
    List<Book> searchBooks(String searchedValue, List<Long> genres, Long authorId, boolean searchOnlyAvailable);
    Book getBook(Long id);
    int getAvailableCount(Book book);
    int getAvailableCount(Long id);
    Book createBook(BookRequest request);
    Book updateBook(BookRequest request);
    void deleteBook(Long bookId);
}
