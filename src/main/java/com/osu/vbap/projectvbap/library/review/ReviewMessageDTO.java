package com.osu.vbap.projectvbap.library.review;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMessageDTO {

    private String name;
    private String comment;
    private int rating;
}
