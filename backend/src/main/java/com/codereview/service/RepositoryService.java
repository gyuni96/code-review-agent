package com.codereview.service;

import com.codereview.domain.Repository;
import com.codereview.domain.User;
import com.codereview.repository.RepositoryRepository;
import com.codereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubClient githubClient;
    private final RepositoryRepository repositoryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<Repository> syncRepositories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String reposJson = githubClient.getUserRepositories(user.getAccessToken()).block();
        
        try {
            List<Map<String, Object>> reposData = objectMapper.readValue(reposJson, new TypeReference<>() {});
            
            return reposData.stream().map(data -> {
                Long githubRepoId = ((Number) data.get("id")).longValue();
                String fullName = (String) data.get("full_name");
                
                return repositoryRepository.findByGithubRepoId(githubRepoId)
                        .orElseGet(() -> {
                            Repository newRepo = Repository.builder()
                                    .githubRepoId(githubRepoId)
                                    .fullName(fullName)
                                    .user(user)
                                    .isActive(false)
                                    .build();
                            return repositoryRepository.save(newRepo);
                        });
            }).collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Failed to sync repositories: {}", e.getMessage());
            throw new RuntimeException("Sync failed", e);
        }
    }

    @Transactional
    public void toggleActive(Long repoId, boolean active) {
        Repository repo = repositoryRepository.findById(repoId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        repo.setIsActive(active);
        repositoryRepository.save(repo);
    }
}
