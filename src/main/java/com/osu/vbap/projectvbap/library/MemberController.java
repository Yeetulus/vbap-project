package com.osu.vbap.projectvbap.library;

import com.osu.vbap.projectvbap.library.loan.Loan;
import com.osu.vbap.projectvbap.library.loan.LoanService;
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import com.osu.vbap.projectvbap.library.review.ReviewMessageDTO;
import com.osu.vbap.projectvbap.library.review.ReviewRequest;
import com.osu.vbap.projectvbap.library.review.ReviewService;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final LoanService loanService;
    private final ReviewService reviewService;

// region Reservation

    @PostMapping("/reservation/create")
    public ResponseEntity<Reservation> reserveBook(
            @RequestParam Long bookId,
            HttpServletRequest request
    ){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(reservationService.createReservation(bookId, jwtUser.getId()));
    }

    @DeleteMapping("/reservation/cancel")
    public ResponseEntity<Void> cancelReservation(
            @RequestParam Long bookId,
            HttpServletRequest request
    ){
        User jwtUser = userService.getUserFromRequest(request);
        reservationService.cancelReservation(bookId, jwtUser.getId());
        return ResponseEntity.ok().build();
    }

// endregion Reservation

// region Loan

    @PutMapping("/loan/extend")
    public ResponseEntity<Loan> extendLoan(
            @RequestParam Long loanId,
            @RequestParam(required = false) @Min(1) @Max(30) Integer duration,
            HttpServletRequest request){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(loanService.extendLoan(loanId, jwtUser.getId(), duration));
    }

    @GetMapping("/loan/get-all")
    public ResponseEntity<List<Loan>> getLoans(
            HttpServletRequest request){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(loanService.getAllLoansByUser(jwtUser));
    }

    @GetMapping("/loan/get-all-active")
    public ResponseEntity<List<Loan>> getActiveLoans(
            HttpServletRequest request){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(loanService.getAllActiveLoansByUser(jwtUser));
    }

// endregion Loan

// region Review

    @PostMapping("/review/create")
    public ResponseEntity<ReviewMessageDTO> createReview(
            @Valid @RequestBody ReviewRequest reviewRequest,
            HttpServletRequest request) {
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewRequest, jwtUser));
    }

    @PutMapping("/review/edit")
    public ResponseEntity<ReviewMessageDTO> editReview(
            @RequestParam Long reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest,
            HttpServletRequest request) {
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewId, reviewRequest, jwtUser));
    }

    @GetMapping("review/get-all")
    public ResponseEntity<List<ReviewMessageDTO>> getAllUserReviews(
            HttpServletRequest request){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(reviewService.getAllUserReviews(jwtUser));
    }

    @DeleteMapping("/review/delete")
    public ResponseEntity<Void> deleteReview(
            @RequestParam Long reviewId,
            HttpServletRequest request) {
        User jwtUser = userService.getUserFromRequest(request);
        reviewService.deleteReview(jwtUser, reviewId);
        return ResponseEntity.ok().build();
    }

// endregion Review

}
