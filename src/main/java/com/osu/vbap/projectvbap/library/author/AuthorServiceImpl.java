package com.osu.vbap.projectvbap.library.author;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;
import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageName;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }
    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name.toLowerCase()).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, name)));
    }
    @Override
    public Author createAuthor(String name) {
        var newAuthor = Author.builder()
                        .name(name.toLowerCase()).build();
        return authorRepository.save(newAuthor);
    }
    @Override
    public Author updateAuthor(Long id, String newName) {
        var existingAuthor = authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
        existingAuthor.setName(newName.toLowerCase());
        return authorRepository.save(existingAuthor);
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        var authorToDelete = authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
        authorRepository.delete(authorToDelete);
        return true;
    }

    @Override
    public boolean deleteAuthorByName(String name) {
        var authorToDelete = authorRepository.findByName(name).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, name)));
        authorRepository.delete(authorToDelete);
        return true;
    }

}