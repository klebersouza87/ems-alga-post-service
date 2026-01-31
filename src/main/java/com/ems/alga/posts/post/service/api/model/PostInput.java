package com.ems.alga.posts.post.service.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostInput {

    @NotBlank(message = "title field is mandatory")
    private String title;
    @NotBlank(message = "body field is mandatory")
    private String body;
    @NotBlank(message = "author field is mandatory")
    private String author;

}