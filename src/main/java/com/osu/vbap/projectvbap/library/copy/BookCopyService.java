package com.osu.vbap.projectvbap.library.copy;

import java.util.List;

public interface BookCopyService {

    BookCopy createCopy(Long bookId);
    BookCopy updateCopy(Long copyId, BookCopyCondition condition);
    BookCopy updateCopy(BookCopy copy, BookCopyCondition condition);
    BookCopy getCopy(Long copyId);
    List<BookCopy> getCopiesByBook(Long bookId);
    void deleteCopy(Long copyId);
}
