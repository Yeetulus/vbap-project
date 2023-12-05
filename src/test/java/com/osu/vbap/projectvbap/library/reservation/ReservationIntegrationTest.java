package com.osu.vbap.projectvbap.library.reservation;

import com.osu.vbap.projectvbap.ProjectVbapApplication;
import com.osu.vbap.projectvbap.library.book.Book;
import com.osu.vbap.projectvbap.library.book.BookRepository;
import com.osu.vbap.projectvbap.library.copy.BookCopy;
import com.osu.vbap.projectvbap.library.copy.BookCopyCondition;
import com.osu.vbap.projectvbap.library.copy.BookCopyRepository;
import com.osu.vbap.projectvbap.library.genre.Genre;
import com.osu.vbap.projectvbap.library.genre.GenreRepository;
import com.osu.vbap.projectvbap.user.Role;
import com.osu.vbap.projectvbap.user.User;
import com.osu.vbap.projectvbap.user.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProjectVbapApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Test
    public void createReservation(){

        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("mail@seznam.cz")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();

        Book book = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        BookCopy copy1 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();
        BookCopy copy2 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();
        BookCopy copy3 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();

        user = userRepository.save(user);
        genreRepository.save(genre);
        book = bookRepository.save(book);
        bookCopyRepository.save(copy1);
        bookCopyRepository.save(copy2);
        bookCopyRepository.save(copy3);

        try {
            mvc.perform(post("/api/member/reservation/create")
                    .requestAttr("jwtUser", user)
                    .param("bookId", book.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.user.email").value("mail@seznam.cz"))
                    .andExpect(jsonPath("$.user.firstName").value("Jan"))
                    .andExpect(jsonPath("$.user.lastName").value("Novák"))
                    .andExpect(jsonPath("$.book.title").value("Hamlet"))
                    .andExpect(jsonPath("$.book.genre.name").value("Drama"));

        } catch (Exception e) {
            fail();
        }

    }
    @Test
    public void createReservation_AlreadyReserved(){

        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("mail@seznam.cz")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();

        Book book = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        BookCopy copy1 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();
        BookCopy copy2 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();
        BookCopy copy3 = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE).build();

        user = userRepository.save(user);
        genreRepository.save(genre);
        book = bookRepository.save(book);
        bookCopyRepository.save(copy1);
        bookCopyRepository.save(copy2);
        bookCopyRepository.save(copy3);

        try {
            mvc.perform(post("/api/member/reservation/create")
                            .requestAttr("jwtUser", user)
                            .param("bookId", book.getId().toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            fail();
        }

        try {
            mvc.perform(post("/api/member/reservation/create")
                            .requestAttr("jwtUser", user)
                            .param("bookId", book.getId().toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(String.format("Book %s is already reserved", book.getId())));

        } catch (Exception e) {
            fail();
        }

    }
    @Test
    public void createReservation_NotAvailable(){

        User user = User.builder()
                .firstName("Jan")
                .lastName("Novák")
                .email("mail@seznam.cz")
                .password("password")
                .role(Role.MEMBER)
                .build();

        Genre genre = Genre.builder().name("Drama").build();

        Book book = Book.builder()
                .title("Hamlet")
                .genre(genre)
                .build();

        user = userRepository.save(user);
        genreRepository.save(genre);
        book = bookRepository.save(book);

        try {
            mvc.perform(post("/api/member/reservation/create")
                            .requestAttr("jwtUser", user)
                            .param("bookId", book.getId().toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(String.format("Book with ID %s is not available", book.getId())));

        } catch (Exception e) {
            fail();
        }

    }
}
