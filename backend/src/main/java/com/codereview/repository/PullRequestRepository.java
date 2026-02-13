package com.codereview.repository;

import com.codereview.domain.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    Optional<PullRequest> findByGithubPrId(Long githubPrId);
}
