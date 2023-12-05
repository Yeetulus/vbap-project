package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.book.BookService;
import com.osu.vbap.projectvbap.user.Role;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;


    @Test
    void createReservation() {

        Book book = Book.builder()
                .id(1L)
                .title("Hamlet")
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Novak")
                .email("mail@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        when(bookService.getBook(Mockito.any(Long.class))).thenReturn(book);
        when(userService.getUser(Mockito.any(Long.class))).thenReturn(user);
        when(reservationRepository.existsByUserAndBook(Mockito.any(User.class), Mockito.any(Book.class))).thenReturn(false);
        when(bookService.getAvailableCount(Mockito.any(Book.class))).thenReturn(1);
        when(reservationRepository.save(Mockito.any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        var reservation = reservationService.createReservation(book.getId(), user.getId());

        Assertions.assertThat(book).isNotNull();
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(reservation).isNotNull();
        Assertions.assertThat(reservation.getBook()).isEqualTo(book);
        Assertions.assertThat(reservation.getUser()).isEqualTo(user);

    }

    @Test
    void cancelPotentialReservation_Exists() {

        Long userId = 1L;
        Long bookId = 1L;

        Reservation toFind = Reservation.builder().build();
        AtomicReference<Reservation> toFindReference = new AtomicReference<>(toFind);

        when(reservationRepository.findByBook_IdAndUser_Id(Mockito.any(Long.class), Mockito.any(Long.class)))
                .thenReturn(Optional.of(toFindReference.get()));

        doAnswer(invocation -> {
            toFindReference.set(null);
            return null;
        }).when(reservationRepository).delete(toFindReference.get());

        reservationService.cancelPotentialReservation(bookId, userId);

        Assertions.assertThat(toFindReference.get()).isNull();

    }

    @Test
    void cancelPotentialReservation_NotExists() {

        Long userId = 1L;
        Long bookId = 1L;
        when(reservationRepository.findByBook_IdAndUser_Id(Mockito.any(Long.class), Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        reservationService.cancelPotentialReservation(bookId, userId);

        verify(reservationRepository, times(0)).delete(Mockito.any(Reservation.class));
    }

    @Test
    void cancelReservation() {
        Long userId = 1L;
        Long bookId = 1L;
        Long testingId = 10L;

        when(reservationRepository.findByBook_IdAndUser_Id(Mockito.any(Long.class), Mockito.any(Long.class)))
                .thenReturn(Optional.of(Reservation.builder()
                        .id(testingId)
                        .build()));
        AtomicReference<Reservation> arg = new AtomicReference<>(null);
        doAnswer(invocation -> {
            arg.set((Reservation)invocation.getArguments()[0]);
            return null;
        }).when(reservationRepository).delete(Mockito.any(Reservation.class));

        reservationService.cancelReservation(bookId, userId);

        Assertions.assertThat(arg.get()).isNotNull();
        Assertions.assertThat(arg.get().getId()).isEqualTo(testingId);

    }
    @Test
    void cancelReservation_Exception() {
        Long userId = 1L;
        Long bookId = 1L;

        when(reservationRepository.findByBook_IdAndUser_Id(Mockito.any(Long.class), Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> reservationService.cancelReservation(bookId, userId));
    }

    @Test
    void getUserReservations() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .firstName("Jan")
                .lastName("Novak")
                .email("mail@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        List<Reservation> reservationList = new ArrayList<>(){{
            add(Reservation.builder().user(user).build());
            add(Reservation.builder().user(user).build());
            add(Reservation.builder().build());
            add(Reservation.builder().build());
            add(Reservation.builder().build());
        }};

        when(userService.getUser(Mockito.any(Long.class))).thenReturn(user);
        when(reservationRepository.findAllByUser(Mockito.any(User.class)))
                .then(i -> reservationList.stream().filter(r -> r.getUser() == user).toList());

        List<Reservation> resultList = reservationService.getUserReservations(userId);

        Assertions.assertThat(resultList.size()).isEqualTo(2);
        resultList.forEach(r -> Assertions.assertThat(r.getUser()).isEqualTo(user));

    }
    @Test
    void getBookReservations() {
        Long bookId = 1L;
        Book book = Book.builder()
                .id(bookId)
                .title("Hamlet")
                .build();


        List<Reservation> reservationList = new ArrayList<>(){{
            add(Reservation.builder().book(book).build());
            add(Reservation.builder().book(book).build());
            add(Reservation.builder().build());
            add(Reservation.builder().build());
            add(Reservation.builder().build());
        }};

        when(bookService.getBook(Mockito.any(Long.class))).thenReturn(book);
        when(reservationRepository.findAllByBook(Mockito.any(Book.class)))
                .then(i -> reservationList.stream().filter(r -> r.getBook() == book).toList());

        List<Reservation> resultList = reservationService.getBookReservations(bookId);

        Assertions.assertThat(resultList.size()).isEqualTo(2);
        resultList.forEach(r -> Assertions.assertThat(r.getBook()).isEqualTo(book));

    }
}