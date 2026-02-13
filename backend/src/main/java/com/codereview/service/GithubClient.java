package com.codereview.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GithubClient {

    private final WebClient webClient;

    public GithubClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    public Mono<String> getPullRequestDiff(String owner, String repo, int prNumber, String accessToken) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{prNumber}", owner, repo, prNumber)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github.v3.diff")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Void> postComment(String owner, String repo, int prNumber, String body, String accessToken) {
        return webClient.post()
                .uri("/repos/{owner}/{repo}/issues/{prNumber}/comments", owner, repo, prNumber)
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(Map.of("body", body))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<String> getUserRepositories(String accessToken) {
        return webClient.get()
                .uri("/user/repos?sort=updated&per_page=100")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class);
    }
}
