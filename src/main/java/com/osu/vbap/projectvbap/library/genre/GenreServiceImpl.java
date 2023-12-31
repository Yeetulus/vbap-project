package com.osu.vbap.projectvbap.library.genre;

import com.osu.vbap.projectvbap.exception.ItemAlreadyExistsException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.*;

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
        if(genreRepository.existsByName(genreName))
            throw new ItemAlreadyExistsException(String.format(alreadyExistsMessageName, "Genre", genreName));

        var newGenre = Genre.builder()
                .name(genreName).build();

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
    public void deleteGenre(String genreName) {
        var toDelete = genreRepository.findByName(genreName).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, genreName)));

        delete(toDelete);
    }

    @Override
    public void deleteGenre(Long genreId) {
        var toDelete = genreRepository.findById(genreId).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, genreId)));

        delete(toDelete);
    }

    private void delete(Genre genre){
        if(!genre.getBooks().isEmpty())
            throw new IllegalStateException(String.format(foundReferencesMessage, "genre"));
        genreRepository.delete(genre);
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

}