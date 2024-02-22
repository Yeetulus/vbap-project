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
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@SecurityRequirement(name = "bearerAuth")
public class LibrarianController {

    private final LoanService loanService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookCopyService bookCopyService;
    private final BookService bookService;
    private final ReservationService reservationService;
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id
    ) {
        if(name == null && id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , paramMessage);
        }
        else if (name != null) genreService.deleteGenre(name);
        else genreService.deleteGenre(id);

        return ResponseEntity.ok().build();
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
        else if (id != null) authorService.deleteAuthorById(id);
        else authorService.deleteAuthorByName(name);

        return ResponseEntity.ok().build();
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
    @GetMapping("/loan/get")
    public ResponseEntity<Loan> getLoan(@RequestParam Long loanId){
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }
    @GetMapping("/loan/user-loans")
    public ResponseEntity<List<Loan>> getUserLoans(@RequestParam Long userId) {
        return ResponseEntity.ok(loanService.getAllLoansByUserId(userId));
    }
    @GetMapping("/loan/user-loans-active")
    public ResponseEntity<List<Loan>> getActiveUserLoans(@RequestParam Long userId) {
        return ResponseEntity.ok(loanService.getAllActiveLoansByUser(userId));
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
    @DeleteMapping("/book/delete")
    public ResponseEntity<Void> updateBook(@RequestParam Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }


// endregion Books

// region Copies

    @PostMapping("/copy/create")
    public ResponseEntity<BookCopy> createBookCopy(@RequestParam Long bookId){
        return ResponseEntity.ok(bookCopyService.createCopy(bookId));
    }
    @GetMapping("/copy/get")
    public ResponseEntity<BookCopy> getBookCopy(@RequestParam Long copyId){
        return ResponseEntity.ok(bookCopyService.getCopy(copyId));
    }
    @GetMapping("/copy/get-all")
    public ResponseEntity<List<BookCopy>> getAllCopies(@RequestParam Long bookId){
        return ResponseEntity.ok(bookCopyService.getCopiesByBook(bookId));
    }
    @PutMapping("/copy/update")
    public ResponseEntity<BookCopy> updateBookCopy(@RequestParam Long copyId,
                                                   @RequestParam BookCopyCondition condition){
        return ResponseEntity.ok(bookCopyService.updateCopy(copyId, condition));
    }

    @DeleteMapping("/copy/delete")
    public ResponseEntity<Void> deleteBookCopy(@RequestParam Long copyId){
        bookCopyService.deleteCopy(copyId);
        return ResponseEntity.ok().build();
    }


// endregion Copies

// region Reservation

    @GetMapping("/reservation/get-all-user")
    public ResponseEntity<List<Reservation>> getUserReservations(@RequestParam Long userId){
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/reservation/get-all-book")
    public ResponseEntity<List<Reservation>> getBookReservations(@RequestParam Long bookId){
        return ResponseEntity.ok(reservationService.getBookReservations(bookId));
    }

// endregion Reservation

}
