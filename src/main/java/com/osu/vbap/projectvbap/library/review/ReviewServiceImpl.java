package com.osu.vbap.projectvbap.library.review;

import com.osu.vbap.projectvbap.exception.ItemAlreadyExistsException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.exception.ItemNotOwnedException;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.library.loan.LoanService;
import com.osu.vbap.projectvbap.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final LoanService loanService;

    @Override
    public ReviewMessageDTO createReview(ReviewRequest request, User user) {

        var book = bookService.getBook(request.getBookId());

        var loans = loanService.getAllLoansByUser(user);
        if(loans.stream().noneMatch(l -> l.getCopy().getBook().getId().equals(book.getId())))
            throw new ItemNotFoundException(String.format("User never borrowed book with ID %s", book.getId()));

        if(reviewRepository.existsByUserAndBook(user, book))
            throw new ItemAlreadyExistsException(String.format(reviewAlreadyExists, book.getId(), user.getId()));

        Review review = Review.builder()
                .book(book)
                .rating(request.getRating())
                .comment(request.getComment())
                .user(user)
                .build();

        review = reviewRepository.save(review);

        return buildReviewMessage(review);
    }

    private ReviewMessageDTO buildReviewMessage(Review review) {
        return ReviewMessageDTO.builder()
                .bookId(review.getBook().getId())
                .rating(review.getRating())
                .name(review.getUser().getFirstName())
                .comment(review.getComment())
                .build();
    }

    @Override
    public ReviewMessageDTO updateReview(Long reviewId, ReviewRequest reviewRequest, User user) {
        var review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, reviewId)));

        if(!review.getUser().getId().equals(user.getId()))
            throw new ItemNotOwnedException(String.format(notOwnedMessage, reviewId));

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        var saved = reviewRepository.save(review);
        return buildReviewMessage(saved);
    }

    @Override
    public List<ReviewMessageDTO> getAllUserReviews(User user) {
        var reviews = reviewRepository.findAllByUser(user);
        return createReviewDTOList(reviews);
    }

    private List<ReviewMessageDTO> createReviewDTOList(List<Review> reviews){
        return reviews.stream()
                .map(r -> ReviewMessageDTO.builder()
                        .bookId(r.getBook().getId())
                        .name(String.format("%s", r.getUser().getFirstName()))
                        .comment(r.getComment())
                        .rating(r.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ReviewsDTO getReviewData(Long bookId) {
        var reviews = reviewRepository.findAllByBook_Id(bookId);

        var reviewsWithComments = reviews.stream().filter(review ->
                (review.getComment() != null
                && !review.getComment().isBlank()))
                .toList();
        List<ReviewMessageDTO> messages = createReviewDTOList(reviewsWithComments);

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
    public void deleteReview(User user, Long id) {
        var review = reviewRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));

        if(!review.getUser().getId().equals(user.getId()))
            throw new ItemNotOwnedException(String.format(notOwnedMessage, id));

        reviewRepository.delete(review);
    }
}