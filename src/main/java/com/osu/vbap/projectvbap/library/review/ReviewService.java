package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;

import java.util.List;

public interface ReviewService {
    ReviewMessageDTO createReview(ReviewRequest request, User user);
    ReviewMessageDTO updateReview(Long reviewId, ReviewRequest request, User user);
    List<ReviewMessageDTO> getAllUserReviews(User user);
    List<ReviewMessageDTO> getAllBookReviews(Book book);
    ReviewsDTO getReviewData(Long bookId);
    Review getReviewById(Long id);
    void deleteReview(User user, Long id);
}
