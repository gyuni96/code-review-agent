package com.codereview.service;

import com.codereview.dto.ReviewJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewWorker {

    private final ReviewQueueService queueService;
    private final GithubClient githubClient;
    private final GroqClient groqClient;

    @Scheduled(fixedDelay = 5000) // Poll every 5 seconds
    public void processJobs() {
        ReviewJob job = queueService.dequeue();
        while (job != null) {
            log.info("Processing review job for PR #{} in {}/{}", job.getPrNumber(), job.getOwner(), job.getRepo());
            try {
                processJob(job);
            } catch (Exception e) {
                log.error("Failed to process job: {}", e.getMessage(), e);
            }
            job = queueService.dequeue();
        }
    }

    private void processJob(ReviewJob job) {
        githubClient.getPullRequestDiff(job.getOwner(), job.getRepo(), job.getPrNumber(), job.getAccessToken())
                .flatMap(diff -> groqClient.analyzeCode(diff))
                .flatMap(review -> githubClient.postComment(job.getOwner(), job.getRepo(), job.getPrNumber(), review, job.getAccessToken()))
                .doOnSuccess(v -> log.info("Successfully posted AI review for PR #{}", job.getPrNumber()))
                .doOnError(e -> log.error("Error processing AI review: {}", e.getMessage()))
                .subscribe();
    }
}
