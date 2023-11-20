package com.osu.vbap.projectvbap.library.author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(Long id);
    Author getAuthorByName(String name);

    Author createAuthor(String name);

    Author updateAuthor(Long id, String newName);

    boolean deleteAuthorById(Long id);
    boolean deleteAuthorByName(String name);
}
