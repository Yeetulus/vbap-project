package com.osu.vbap.projectvbap.user;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.osu.vbap.projectvbap.auth.model.AuthResponse;
import com.osu.vbap.projectvbap.auth.model.RegisterRequest;
import com.osu.vbap.projectvbap.library.author.Author;
import com.osu.vbap.projectvbap.library.author.AuthorService;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import com.osu.vbap.projectvbap.library.loan.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final BookService bookService;
    private final LoanService loanService;
    private final GenreService genreService;
    private final AuthorService authorService;

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

    @GetMapping("genre/get")
    public ResponseEntity<Genre> getGenre(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name){
        if(id == null && name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , paramMessage);
        }
        else if (id != null) return ResponseEntity.ok(genreService.getById(id));
        else return ResponseEntity.ok(genreService.getByName(name));
    }

    @GetMapping("genre/get-all")
    public ResponseEntity<List<Genre>> getAllGenres(){
        return ResponseEntity.ok(genreService.getAllGenres());
    }

// endregion Genre
// region Author
    @PostMapping("/author/create")
    public ResponseEntity<Author> createAuthor(
            @NotBlank @RequestParam String name
    ) {
        return ResponseEntity.ok(authorService.createAuthor(name));
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






// endregion Loans

}
