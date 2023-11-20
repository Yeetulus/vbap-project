package com.osu.vbap.projectvbap.library.genre;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;
import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageName;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getGenres(List<String> genres) {
        if(genres != null && !genres.isEmpty()){
            return genreRepository.findByNameIn(genres);
        }
        return genreRepository.findAll();
    }
    @Override
    public Genre createGenre(String genreName) {
        var newGenre = Genre.builder()
                .name(genreName.toLowerCase(Locale.ROOT)).build();
        return genreRepository.save(newGenre);
    }
    @Override
    public Genre updateGenre(String genreName, String newName) {
        var existingGenre = genreRepository.findByName(genreName).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, genreName)));
        existingGenre.setName(newName);
        return genreRepository.save(existingGenre);
    }
    @Override
    public boolean deleteGenre(String genreName) {
        var toDelete = genreRepository.findByName(genreName).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, genreName)));
        genreRepository.delete(toDelete);
        return true;
    }

    @Override
    public Genre getById(Long id) {
        return genreRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }

    @Override
    public Genre getByName(String name) {
        return genreRepository.findByName(name).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, name)));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}