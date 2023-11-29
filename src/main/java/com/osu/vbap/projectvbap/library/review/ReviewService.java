package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;

import java.util.List;

public interface ReviewService {
    Review saveReview(Review review);
    List<Review> getAllUserReviews(User user);
    List<Review> getAllBookReviews(Book book);
    ReviewsDTO getReviewData(Long bookId);
    Review getReviewById(Long id);
    void deleteReview(Long id);
}
