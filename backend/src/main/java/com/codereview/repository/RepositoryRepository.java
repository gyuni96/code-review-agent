package com.codereview.repository;

import com.codereview.domain.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    Optional<Repository> findByGithubRepoId(Long githubRepoId);
}
