package com.osu.vbap.projectvbap.library.author;

import com.osu.vbap.projectvbap.exception.ItemAlreadyExistsException;
import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.*;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAuthorsByIds(List<Long> ids) {
        return authorRepository.findAllById(ids);
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }
    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, name)));
    }
    @Override
    public Author createAuthor(String name) {
        if(authorRepository.existsByName(name))
            throw new ItemAlreadyExistsException(String.format(alreadyExistsMessageName, "Author", name));

        var newAuthor = Author.builder()
                        .name(name).build();
        return authorRepository.save(newAuthor);
    }
    @Override
    public Author updateAuthor(Long id, String newName) {
        var existingAuthor = authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
        existingAuthor.setName(newName);
        return authorRepository.save(existingAuthor);
    }

    @Override
    public void deleteAuthorById(Long id) {
        var authorToDelete = authorRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));

        delete(authorToDelete);
    }

    @Override
    public void deleteAuthorByName(String name) {
        var authorToDelete = authorRepository.findByName(name).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageName, name)));

        delete(authorToDelete);
    }

    private void delete(Author author){
        if(!author.getBooks().isEmpty())
            throw new IllegalStateException(String.format(foundReferencesMessage, "author"));

        authorRepository.delete(author);
    }

}