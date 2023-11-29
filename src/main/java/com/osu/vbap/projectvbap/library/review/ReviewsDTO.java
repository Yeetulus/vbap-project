package com.osu.vbap.projectvbap.library.review;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsDTO {

    private List<ReviewMessageDTO> messages;
    private int reviewsCount;
    private float average;
}
