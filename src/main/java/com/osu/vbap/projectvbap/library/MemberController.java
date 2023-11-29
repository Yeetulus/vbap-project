package com.osu.vbap.projectvbap.library;

import com.osu.vbap.projectvbap.jwt.JwtService;
import com.osu.vbap.projectvbap.library.loan.Loan;
import com.osu.vbap.projectvbap.library.loan.LoanService;
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final LoanService loanService;

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

    @PutMapping("/loan/extend")
    public ResponseEntity<Loan> extendLoan(
            @RequestParam Long loanId,
            @RequestParam(required = false) @Min(1) Integer duration,
            HttpServletRequest request){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(loanService.extendLoan(loanId, jwtUser.getId(), duration));
    }

}
