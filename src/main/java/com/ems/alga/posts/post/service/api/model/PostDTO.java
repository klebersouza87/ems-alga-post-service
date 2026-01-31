package com.ems.alga.posts.post.service.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private String postId;
    private String postBody;
    private Long wordCount;
    private Double calculatedValue;

}
