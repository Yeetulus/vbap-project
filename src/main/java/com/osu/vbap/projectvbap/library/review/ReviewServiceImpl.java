package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(()->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }
    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}