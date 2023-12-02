package com.osu.vbap.projectvbap.library.author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();
    List<Author> getAuthorsByIds(List<Long> ids);

    Author getAuthorById(Long id);
    Author getAuthorByName(String name);

    Author createAuthor(String name);
    Author updateAuthor(Long id, String newName);

    void deleteAuthorById(Long id);
    void deleteAuthorByName(String name);
}
