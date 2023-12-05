package com.osu.vbap.projectvbap.library.genre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);
    boolean existsByName(String name);
    List<Genre> findByNameIn(List<String> names);
}
