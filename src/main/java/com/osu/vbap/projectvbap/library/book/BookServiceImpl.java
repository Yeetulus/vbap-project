package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.author.AuthorService;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.copy.BookCopyService;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final BookCopyService bookCopyService;
    private final AuthorService authorService;

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
        List<BookCopy> copies = bookCopyService.getCopiesByBook(book);
        for (BookCopy copy : copies) {
            if (copy.getBookCondition().equals(BookCopyCondition.AVAILABLE)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Book createBook(BookRequest request) {
        var authors = request.getAuthorIds().stream()
                .map(authorService::getAuthorById)
                .collect(Collectors.toSet());

        var genre = genreService.getById(request.getGenreId());

        var book = Book.builder()
                .genre(genre)
                .authors(authors)
                .title(request.getTitle())
                .build();

        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(BookRequest request) {
        if(request.getBookId()==null) throw new NullPointerException("Book id not found");

        var book = getBook(request.getBookId());
        var genre = genreService.getById(request.getGenreId());
        var authors = authorService.getAuthorsByIds(request.getAuthorIds());

        book.setTitle(request.getTitle());
        book.setGenre(genre);
        book.setAuthors(new HashSet<>(authors));

        return bookRepository.save(book);
    }
}
