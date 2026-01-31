package com.ems.alga.posts.post.service.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryOutput {

    private String id;
    private String title;
    private String summary;
    private String author;

}
