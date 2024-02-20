package com.osu.vbap.projectvbap.library.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    @Nullable
    private Long bookId;

    @NotBlank
    private String title;

    @NotNull
    private Long pages;

    private Date releaseDate;

    @NotNull
    private Long genreId;

    private List<Long> authorIds;
}
