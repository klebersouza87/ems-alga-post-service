package com.ems.alga.posts.post.service.domain.service;

import com.ems.alga.posts.post.service.api.model.PostDTO;
import com.ems.alga.posts.post.service.api.model.PostInput;
import com.ems.alga.posts.post.service.api.model.PostOutput;
import com.ems.alga.posts.post.service.api.model.PostSummaryOutput;
import com.ems.alga.posts.post.service.domain.model.Post;
import com.ems.alga.posts.post.service.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.ems.alga.posts.post.service.common.UuidGenerator.generateTimeBasedUUID;
import static com.ems.alga.posts.post.service.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_TEXT_PROCESSOR_SERVICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    @Transactional
    public PostOutput createPost(PostInput postInput) {
        Post post = Post.builder()
                .id(generateTimeBasedUUID())
                .title(postInput.getTitle())
                .body(postInput.getBody())
                .author(postInput.getAuthor())
                .build();

        postRepository.save(post);

        PostDTO postDTO = PostDTO.builder()
                .postId(post.getId().toString())
                .postBody(post.getBody())
                .build();

        rabbitTemplate.convertAndSend(QUEUE_TEXT_PROCESSOR_SERVICE, postDTO);

        return PostOutput.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getAuthor())
                .build();
    }

    public void handleProcessedMessage(PostDTO postDTO) {
        //TODO if below is only for testing purpose, remove it later. Used to simulate error and test Dead Letter Queue functionality.
        if (postDTO.getCalculatedValue() < 2.5) {
            throw new RuntimeException("Simulated error during post creation for testing Dead Letter Queue purposes.");
        }

        postRepository.findById(UUID.fromString(postDTO.getPostId()))
                .ifPresentOrElse(post -> {
                    post.setWordCount(postDTO.getWordCount());
                    post.setCalculatedValue(postDTO.getCalculatedValue());
                    postRepository.save(post);
                    log.info("Updated Post ID: {} with word count: {} and calculated value: {}",
                            post.getId(), post.getWordCount(), post.getCalculatedValue());
                }, () -> log.warn("Post with ID: {} not found for updating", postDTO.getPostId()));
    }

    public PostOutput findPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with ID: " + postId));

        return PostOutput.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getAuthor())
                .build();
    }

    public Page<PostSummaryOutput> findAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::convertToPostSummaryOutput);
    }

    private PostSummaryOutput convertToPostSummaryOutput(Post post) {
        StringBuilder firstThreeLines = new StringBuilder();
        post.getBody().lines().limit(3).forEach(line -> firstThreeLines.append(line).append(" "));

        return PostSummaryOutput.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .author(post.getAuthor())
                .summary(firstThreeLines.toString().trim())
                .build();
    }

}
