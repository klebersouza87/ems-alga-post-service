package com.ems.alga.posts.post.service.api.controller;

import com.ems.alga.posts.post.service.api.model.PostInput;
import com.ems.alga.posts.post.service.api.model.PostOutput;
import com.ems.alga.posts.post.service.api.model.PostSummaryOutput;
import com.ems.alga.posts.post.service.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutput createPost(@RequestBody @Valid PostInput postInput) {
        log.info("Creating new post with title: {}", postInput.getTitle());
        PostOutput postOutput = postService.createPost(postInput);
        log.info("Post created with ID: {} for title: {}", postOutput.getId(), postOutput.getTitle());
        return postOutput;
    }

    @GetMapping("/{postId}")
    public PostOutput findPost(@PathVariable UUID postId) {
        log.info("Fetching post with ID: {}", postId);
        PostOutput postOutput = postService.findPost(postId);
        log.info("Post fetched with ID: {}", postOutput.getId());
        return postOutput;
    }

    @GetMapping
    public Page<PostSummaryOutput> findAllPosts(@PageableDefault Pageable pageable) {
        log.info("Fetching all posts with pagination: page number {}, page size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<PostSummaryOutput> posts = postService.findAllPosts(pageable);
        log.info("Fetched {} posts", posts.getNumberOfElements());
        return posts;
    }

}

