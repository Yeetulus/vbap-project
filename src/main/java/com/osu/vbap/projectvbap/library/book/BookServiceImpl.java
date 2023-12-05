package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.author.AuthorService;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.foundReferencesMessage;
import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final AuthorService authorService;

    public List<Book> searchBooks(String searchedValue, List<Long> genres, boolean searchOnlyAvailable) {
        List<Book> books;
        if (searchedValue != null && !searchedValue.isEmpty() && genres != null && !genres.isEmpty()) {
            books = bookRepository.findByValueAndGenres(genres, searchedValue);
        } else if (genres != null && !genres.isEmpty()) {
            books = bookRepository.findAllByGenreIdIn(genres);
        } else if (searchedValue != null && !searchedValue.isEmpty()) {
            books = bookRepository.findByValue(searchedValue);
        } else {
            books = bookRepository.findAll();
        }

        if (searchOnlyAvailable) {
            books = books.stream().filter(book -> getAvailableCount(book) > 0).toList();
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
        for (BookCopy copy : book.getCopies()) {
            if (copy.getBookCondition().equals(BookCopyCondition.AVAILABLE)
            ) {
                count++;
            }
        }
        return count - book.getReservations().size();
    }

    @Override
    public int getAvailableCount(Long id) {
        var book = getBook(id);
        return getAvailableCount(book);
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
        if(request.getBookId()==null) throw new NullPointerException("Book id not found in request");

        var book = getBook(request.getBookId());
        var genre = genreService.getById(request.getGenreId());
        var authors = authorService.getAuthorsByIds(request.getAuthorIds());

        book.setTitle(request.getTitle());
        book.setGenre(genre);
        book.setAuthors(new HashSet<>(authors));

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        var book = getBook(bookId);
        if(!book.getCopies().isEmpty())
            throw new IllegalStateException(String.format(foundReferencesMessage, "book"));
        bookRepository.delete(book);
    }
}
