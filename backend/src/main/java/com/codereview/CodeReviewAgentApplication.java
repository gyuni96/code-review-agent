package com.codereview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@EnableScheduling
@RestController
@SpringBootApplication
public class 	CodeReviewAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeReviewAgentApplication.class, args);
	}

	@GetMapping("/api/health")
	public Map<String, String> health() {
		return Map.of("status", "UP");
	}

}
