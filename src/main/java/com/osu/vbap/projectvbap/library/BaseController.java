package com.osu.vbap.projectvbap.library;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreService;
import com.osu.vbap.projectvbap.library.review.ReviewsDTO;
import com.osu.vbap.projectvbap.library.review.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class BaseController {

    private final BookService bookService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String searchedValue,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(defaultValue = "false") boolean searchOnlyAvailable
    ) {
        return ResponseEntity.ok(bookService.searchBooks(searchedValue, genreIds, searchOnlyAvailable));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getGenres(
            @RequestParam(required = false) List<String> genreList
    ) {
        return ResponseEntity.ok(genreService.getGenres(genreList));
    }

    @GetMapping("/available")
    public ResponseEntity<Integer> getAvailableCount(@RequestParam Long bookId) {
        return ResponseEntity.ok(bookService.getAvailableCount(bookId));
    }

    @GetMapping("/review")
    public ResponseEntity<ReviewsDTO> getBookReview(@RequestParam Long bookId){
        return ResponseEntity.ok(reviewService.getReviewData(bookId));
    }
}
