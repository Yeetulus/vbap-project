package com.osu.vbap.projectvbap.library.book;

import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final GenreService genreService;

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(value = "searchedValue", required = false) String searchedValue,
            @RequestParam(value = "genres", required = false) List<String> genres,
            @RequestParam(value = "searchAvailable", defaultValue = "false") boolean searchOnlyAvailable
    ) {
        return ResponseEntity.ok(bookService.searchBooks(searchedValue, genres, searchOnlyAvailable));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> searchBooks(
            @RequestParam(value = "genreList", required = false) List<String> genreList
    ) {
        return ResponseEntity.ok(genreService.getGenres(genreList));
    }
}
