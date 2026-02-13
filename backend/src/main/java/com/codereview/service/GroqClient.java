package com.codereview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GroqClient {

    private final WebClient webClient;
    
    @Value("${groq.api.key:}")
    private String apiKey;

    public GroqClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<String> analyzeCode(String diff) {
        if (apiKey == null || apiKey.isEmpty()) {
            return Mono.just("AI Review Mock: API Key is not set.");
        }

        Map<String, Object> requestBody = Map.of(
            "model", "llama3-70b-8192",
            "messages", List.of(
                Map.of("role", "system", "content", "You are a senior software engineer reviewing a pull request. Provide concise, actionable feedback on code quality, bugs, and conventions."),
                Map.of("role", "user", "content", "Please review the following code changes:" + diff)
            )
        );

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                });
    }
}
