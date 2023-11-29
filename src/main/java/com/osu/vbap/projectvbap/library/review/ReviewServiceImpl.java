package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Review> getAllUserReviews(User user) {
        return reviewRepository.findAllByUser(user);
    }

    @Override
    public List<Review> getAllBookReviews(Book book) {
        return reviewRepository.findAllByBook(book);
    }

    @Override
    public ReviewsDTO getReviewData(Long bookId) {
        var reviews = reviewRepository.findAllByBook_Id(bookId);

        var reviewsWithComments = reviews.stream().filter(review -> !review.getComment().isBlank()).toList();
        List<ReviewMessageDTO> messages = reviewsWithComments.stream().map(r ->{
            var user = r.getUser();
            return ReviewMessageDTO.builder()
                    .name(user.getFirstName())
                    .comment(r.getComment())
                    .rating(r.getRating())
                    .build();
        }).toList();

        float average = (float) reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        return ReviewsDTO.builder()
                .reviewsCount(reviews.size())
                .average(average)
                .messages(messages)
                .build();
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