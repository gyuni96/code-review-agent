package com.codereview.controller;

import com.codereview.domain.Repository;
import com.codereview.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/repositories")
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @GetMapping("/sync/{userId}")
    public List<Repository> syncRepositories(@PathVariable Long userId) {
        return repositoryService.syncRepositories(userId);
    }

    @PostMapping("/{repoId}/toggle")
    public void toggleActive(@PathVariable Long repoId, @RequestParam boolean active) {
        repositoryService.toggleActive(repoId, active);
    }
}
