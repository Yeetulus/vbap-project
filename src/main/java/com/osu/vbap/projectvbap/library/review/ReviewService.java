package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.user.User;

import java.util.List;

public interface ReviewService {
    ReviewMessageDTO createReview(ReviewRequest request, User user);
    ReviewMessageDTO updateReview(Long bookId, ReviewRequest request, User user);
    List<ReviewMessageDTO> getAllUserReviews(User user);
    ReviewsDTO getReviewData(Long bookId);
    void deleteReview(User user, Long id);
}
