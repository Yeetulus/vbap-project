package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.copy.BookCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public List<Book> searchBooks(String searchedValue, List<String> genres, boolean searchOnlyAvailable) {
        List<Book> books;
        if (searchedValue != null && !searchedValue.isEmpty() && genres != null && !genres.isEmpty()) {
            books = bookRepository.findByValueAndGenres(genres, searchedValue);
        } else if (genres != null && !genres.isEmpty()) {
            books = bookRepository.findByGenre_NameInIgnoreCase(genres);
        } else if (searchedValue != null && !searchedValue.isEmpty()) {
            books = bookRepository.findByValue(searchedValue);
        } else {
            books = bookRepository.findAll();
        }

        if (searchOnlyAvailable) {
            books = books.stream()
                    .filter(book -> book.getCopies().stream()
                            .allMatch(copy -> copy.getBookCondition().equals(BookCopyCondition.AVAILABLE)))
                    .toList();
        }
        return books;
    }

    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }

    @Override
    public int getAvailableCount(Book book) {
        int count = 0;
        List<BookCopy> copies = bookCopyRepository.findAllByBook(book);
        for (BookCopy copy : copies) {
            if (copy.getBookCondition().equals(BookCopyCondition.AVAILABLE)) {
                count++;
            }
        }
        return count;
    }
}
