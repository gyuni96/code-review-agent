package com.codereview.controller;

import com.codereview.domain.PullRequest;
import com.codereview.domain.Repository;
import com.codereview.dto.ReviewJob;
import com.codereview.repository.PullRequestRepository;
import com.codereview.repository.RepositoryRepository;
import com.codereview.service.ReviewQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final RepositoryRepository repositoryRepository;
    private final PullRequestRepository pullRequestRepository;
    private final ReviewQueueService reviewQueueService;

    @PostMapping("/github")
    public ResponseEntity<Void> handleGithubWebhook(
            @RequestHeader("X-GitHub-Event") String eventType,
            @RequestBody Map<String, Object> payload) {
        
        log.info("Received GitHub Webhook event: {}", eventType);

        if ("pull_request".equals(eventType)) {
            String action = (String) payload.get("action");
            Map<String, Object> prData = (Map<String, Object>) payload.get("pull_request");
            Map<String, Object> repoData = (Map<String, Object>) payload.get("repository");
            
            Long githubRepoId = ((Number) repoData.get("id")).longValue();
            
            if ("opened".equals(action) || "synchronize".equals(action)) {
                repositoryRepository.findByGithubRepoId(githubRepoId).ifPresent(repo -> {
                    if (repo.getIsActive()) {
                        processPullRequest(repo, prData);
                    }
                });
            }
        }

        return ResponseEntity.ok().build();
    }

    private void processPullRequest(Repository repo, Map<String, Object> prData) {
        Long githubPrId = ((Number) prData.get("id")).longValue();
        int prNumber = ((Number) prData.get("number")).intValue();
        String title = (String) prData.get("title");
        String author = (String) ((Map<String, Object>) prData.get("user")).get("login");

        PullRequest pr = pullRequestRepository.findByGithubPrId(githubPrId)
                .orElseGet(() -> PullRequest.builder()
                        .githubPrId(githubPrId)
                        .repository(repo)
                        .prNumber(prNumber)
                        .build());
        
        pr.setTitle(title);
        pr.setAuthor(author);
        pr.setStatus("OPEN");
        pullRequestRepository.save(pr);

        ReviewJob job = ReviewJob.builder()
                .owner(repo.getFullName().split("/")[0])
                .repo(repo.getFullName().split("/")[1])
                .prNumber(prNumber)
                .pullRequestId(pr.getId())
                .accessToken(repo.getUser().getAccessToken())
                .build();

        reviewQueueService.enqueue(job);
        log.info("Enqueued AI review job for PR #{}", prNumber);
    }
}
