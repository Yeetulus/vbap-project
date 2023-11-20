package com.osu.vbap.projectvbap.library.review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review saveReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long id);
    void deleteReview(Long id);
}
