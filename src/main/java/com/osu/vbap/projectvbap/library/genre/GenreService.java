package com.osu.vbap.projectvbap.library.genre;

import org.springframework.http.ProblemDetail;

import java.util.List;

public interface GenreService {

    List<Genre> getGenres(List<String> genres);
    Genre createGenre(String genreName);
    Genre updateGenre(String genreName, String newName);
    void deleteGenre(String genreName);

    Genre getById(Long id);
    Genre getByName(String name);
}
