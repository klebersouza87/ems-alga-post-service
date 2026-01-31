package com.ems.alga.posts.post.service.infrastructure.rabbitmq;

import com.ems.alga.posts.post.service.api.model.PostDTO;
import com.ems.alga.posts.post.service.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.ems.alga.posts.post.service.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESSED_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final PostService postService;

    @RabbitListener(queues = QUEUE_PROCESSED_MESSAGE)
    public void handleMessage(@Payload @Valid PostDTO postDTO) {
        log.info("Received processed message from RabbitMQ. PostId: {}", postDTO.getPostId());
        postService.handleProcessedMessage(postDTO);
        log.info("Processed message handled for Post ID: {}", postDTO.getPostId());
    }

}
