package com.osu.vbap.projectvbap.library.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> search(String searchedValue, List<String> genres, boolean searchOnlyAvailable) {
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
}
