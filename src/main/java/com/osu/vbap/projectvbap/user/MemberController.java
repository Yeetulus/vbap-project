package com.osu.vbap.projectvbap.user;

import com.osu.vbap.projectvbap.jwt.JwtService;
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class MemberController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ReservationService reservationService;

    @PostMapping("/make-reservation/{bookId}")
    public ResponseEntity<Reservation> reserveBook(
            @PathVariable Long bookId,
            @NonNull HttpServletRequest request
    ){
        User jwtUser = userService.getUserFromRequest(request);
        return ResponseEntity.ok(reservationService.createReservation(bookId, jwtUser.getId()));
    }
}
