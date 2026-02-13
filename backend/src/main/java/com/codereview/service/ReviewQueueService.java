package com.codereview.service;

import com.codereview.dto.ReviewJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String QUEUE_KEY = "review:jobs";

    public void enqueue(ReviewJob job) {
        redisTemplate.opsForList().rightPush(QUEUE_KEY, job);
    }

    public ReviewJob dequeue() {
        return (ReviewJob) redisTemplate.opsForList().leftPop(QUEUE_KEY);
    }
}
