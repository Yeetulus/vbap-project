package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.book.BookRepository;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreRepository;
import com.osu.vbap.projectvbap.user.Role;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void save_ConstraintViolationException(){
        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("jan.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();
        Reservation reservation1 = Reservation.builder()
                .book(null)
                .user(user)
                .build();

        userRepository.save(user);
        try {
            reservationRepository.save(reservation1);
        }
        catch (ConstraintViolationException ignored) {}
        catch (Exception e){
            fail();
        }

    }
    @Test
    void findAllByBook() {

        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("jan.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();

        Book book1 = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        Book book2 = Book.builder()
                .title("Macbeth")
                .genre(genre)
                .build();

        Reservation reservation1 = Reservation.builder()
                .book(book1)
                .user(user)
                .build();

        Reservation reservation2 = Reservation.builder()
                .book(book1)
                .user(user)
                .build();

        Reservation reservation3 = Reservation.builder()
                .book(book1)
                .user(user)
                .build();

        Reservation reservation4 = Reservation.builder()
                .book(book2)
                .user(user)
                .build();

        userRepository.save(user);
        genreRepository.save(genre);
        bookRepository.save(book1);
        bookRepository.save(book2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        List<Reservation> all = reservationRepository.findAll();
        List<Reservation> byBook1 = reservationRepository.findAllByBook(book1);
        List<Reservation> byBook2 = reservationRepository.findAllByBook(book2);

        Assertions.assertThat(all).size().isEqualTo(4);
        Assertions.assertThat(byBook1).size().isEqualTo(3);
        Assertions.assertThat(byBook2).size().isEqualTo(1);

    }
    @Test
    void findByBook_IdAndUser_Id() {
        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("jan.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();

        Book book = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        Reservation reservation = Reservation.builder()
                .book(book)
                .user(user)
                .build();

        userRepository.save(user);
        genreRepository.save(genre);
        bookRepository.save(book);

        reservationRepository.save(reservation);
        var opt = reservationRepository.findByBook_IdAndUser_Id(book.getId(), user.getId());
        if(opt.isEmpty()) fail();

        Assertions.assertThat(opt.get()).isNotNull();
        Assertions.assertThat(opt.get().getBook().getId()).isEqualTo(book.getId());
    }
    @Test
    void findAllByUser() {

        User user1 = User.builder()
                .firstName("Jan")
                .lastName("Novak")
                .email("jan.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();
        User user2 = User.builder()
                .firstName("Jana")
                .lastName("Novak")
                .email("jana.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();
        Book book1 = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        Reservation reservation1 = Reservation.builder()
                .book(book1)
                .user(user1)
                .build();
        Reservation reservation2 = Reservation.builder()
                .book(book1)
                .user(user1)
                .build();

        Reservation reservation3 = Reservation.builder()
                .book(book1)
                .user(user2)
                .build();
        Reservation reservation4 = Reservation.builder()
                .book(book1)
                .user(user2)
                .build();
        Reservation reservation5 = Reservation.builder()
                .book(book1)
                .user(user2)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        genreRepository.save(genre);
        bookRepository.save(book1);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);
        reservationRepository.save(reservation5);

        List<Reservation> all = reservationRepository.findAll();
        List<Reservation> byUser1 = reservationRepository.findAllByUser(user1);
        List<Reservation> byUser2 = reservationRepository.findAllByUser(user2);

        Assertions.assertThat(all).size().isEqualTo(5);
        Assertions.assertThat(byUser1).size().isEqualTo(2);
        Assertions.assertThat(byUser2).size().isEqualTo(3);
    }
    @Test
    void existsByUserAndBook() {
        User user1 = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("jan.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();
        User user2 = User.builder()
                .firstName("Jana")
                .lastName("Novák")
                .email("jana.novak@mail.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();
        Book book1 = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        Reservation reservation1 = Reservation.builder()
                .book(book1)
                .user(user1)
                .build();
        Reservation reservation2 = Reservation.builder()
                .book(book1)
                .user(user1)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        genreRepository.save(genre);
        bookRepository.save(book1);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        List<Reservation> all = reservationRepository.findAll();
        boolean exists1 = reservationRepository.existsByUserAndBook(user1, book1);
        boolean exists2 = reservationRepository.existsByUserAndBook(user2, book1);

        Assertions.assertThat(all).size().isEqualTo(2);
        Assertions.assertThat(exists1).isEqualTo(true);
        Assertions.assertThat(exists2).isEqualTo(false);
    }

}