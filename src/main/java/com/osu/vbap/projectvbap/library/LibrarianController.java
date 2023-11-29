package com.osu.vbap.projectvbap.library;

import com.osu.vbap.projectvbap.library.author.Author;
import com.osu.vbap.projectvbap.library.author.AuthorService;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.book.BookRequest;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.copy.BookCopyService;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import com.osu.vbap.projectvbap.library.loan.Loan;
import com.osu.vbap.projectvbap.library.loan.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final LoanService loanService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookCopyService bookCopyService;
    private final BookService bookService;
    private static final String paramMessage = "Either id or name must be present in the request";

// region Genre
    @PostMapping("/genre/create")
    public ResponseEntity<Genre> createGenre(
            @NotBlank @RequestParam String genreName
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreService.createGenre(genreName));
    }
    @PutMapping("/genre/update")
    public ResponseEntity<Genre> updateGenre(
            @NotBlank @RequestParam String genreName,
            @NotBlank @RequestParam String newName
    ) {
        return ResponseEntity.ok(genreService.updateGenre(genreName, newName));
    }
    @DeleteMapping("/genre/delete")
    public ResponseEntity<Boolean> deleteGenre(
            @NotBlank @RequestParam String genreName
    ) {
        return ResponseEntity.ok(genreService.deleteGenre(genreName));
    }

    @GetMapping("/genre/get")
    public ResponseEntity<Genre> getGenre(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name){
        if(id == null && name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , paramMessage);
        }
        else if (id != null) return ResponseEntity.ok(genreService.getById(id));
        else return ResponseEntity.ok(genreService.getByName(name));
    }

// endregion Genre

// region Author
    @PostMapping("/author/create")
    public ResponseEntity<Author> createAuthor(
            @NotBlank @RequestParam String name
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(name));
    }
    @PutMapping("/author/update")
    public ResponseEntity<Author> updateAuthor(
            @RequestParam Long id,
            @RequestParam String newName
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(id, newName));
    }
    @DeleteMapping("/author/delete")
    public ResponseEntity<Boolean> deleteAuthor(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        if(id == null && name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , paramMessage);
        }
        else if (id != null) return ResponseEntity.ok(authorService.deleteAuthorById(id));
        else return ResponseEntity.ok(authorService.deleteAuthorByName(name));
    }
    @GetMapping("/author/get")
    public ResponseEntity<Author> getAuthor(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name){
        if(id == null && name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , paramMessage);
        }
        else if(id != null) return ResponseEntity.ok(authorService.getAuthorById(id));
        else return ResponseEntity.ok(authorService.getAuthorByName(name));
    }

    @GetMapping("/author/get-all")
    public ResponseEntity<List<Author>> getAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

// endregion Author

// region Loans

    @PostMapping("/loan/create")
    public ResponseEntity<Loan> createLoan(
            @RequestParam Long userId,
            @RequestParam Long copyId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoan(userId, copyId));
    }
    @PutMapping("/loan/return")
    public ResponseEntity<Void> updateLoan(
            @RequestParam Long loanId
    ) {
        loanService.returnCopy(loanId);
        return ResponseEntity.ok().build();
    }

// endregion Loans

// region Books

    @PostMapping("/book/create")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest request){
        return ResponseEntity.ok(bookService.createBook(request));
    }
    @PutMapping("/book/update")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody BookRequest request){
        return ResponseEntity.ok(bookService.updateBook(request));
    }


// endregion Books

// region Copies

    @PostMapping("/copy/create")
    public ResponseEntity<BookCopy> createBookCopy(@NonNull @RequestParam Long bookId){
        return ResponseEntity.ok(bookCopyService.createCopy(bookId));
    }
    @PutMapping("/copy/update")
    public ResponseEntity<BookCopy> updateBookCopy(@NonNull @RequestParam Long bookId,
                                                   @NonNull @RequestParam BookCopyCondition condition){
        return ResponseEntity.ok(bookCopyService.updateCopy(bookId, condition));
    }


// endregion Copies

}
