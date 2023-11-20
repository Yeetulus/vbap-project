package com.osu.vbap.projectvbap.library.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre_NameInIgnoreCase(List<String> genres);

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN b.authors a " +
            "LEFT JOIN b.genre g " +
            "WHERE " +
            "(g.name IN :genres) AND " +
            "( :searchedValue IS NULL OR " +
            "  ( LOWER(b.title) LIKE LOWER(CONCAT('%', :searchedValue, '%')) OR " +
            "    LOWER(a.name) LIKE LOWER(CONCAT('%', :searchedValue, '%')) OR " +
            "    LOWER(g.name) LIKE LOWER(CONCAT('%', :searchedValue, '%'))" +
            "  )" +
            ")"
    )
    List<Book> findByValueAndGenres(
            @Param("genres") List<String> genres,
            @Param("searchedValue") String searchedValue
    );


    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN b.authors a " +
            "WHERE " +
            "(:searchValue IS NULL OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(b.genre.name) LIKE LOWER(CONCAT('%', :searchValue, '%')))"
    )
    List<Book> findByValue(@Param("searchValue") String searchValue);

}