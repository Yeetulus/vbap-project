package com.osu.vbap.projectvbap.library.copy;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import com.osu.vbap.projectvbap.library.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.foundReferencesMessage;
import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class BookCopyServiceImpl implements BookCopyService{
    private final BookService bookService;
    private final BookCopyRepository bookCopyRepository;
    @Override
    public BookCopy createCopy(Long bookId) {
        var book = bookService.getBook(bookId);

        var newCopy = BookCopy.builder()
                .book(book)
                .bookCondition(BookCopyCondition.AVAILABLE)
                .build();

        return bookCopyRepository.save(newCopy);
    }

    @Override
    public BookCopy updateCopy(Long copyId, BookCopyCondition condition) {
        var copy = bookCopyRepository.findById(copyId).orElseThrow(()->
                new ItemNotFoundException(String.format(notFoundMessageId,copyId)));

        return updateCopy(copy, condition);
    }

    @Override
    public BookCopy updateCopy(BookCopy copy, BookCopyCondition condition) {
        copy.setBookCondition(condition);
        return bookCopyRepository.save(copy);
    }

    @Override
    public BookCopy getCopy(Long copyId) {
        return bookCopyRepository.findById(copyId).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId,copyId)));
    }

    @Override
    public List<BookCopy> getCopiesByBook(Long bookId) {
        var book = bookService.getBook(bookId);
        return bookCopyRepository.findAllByBook(book);
    }

    @Override
    public void deleteCopy(Long copyId) {
        var copy = getCopy(copyId);
        if(!copy.getLoans().isEmpty())
            throw new IllegalStateException(String.format(foundReferencesMessage, "copy"));
        bookCopyRepository.delete(copy);
    }
}
