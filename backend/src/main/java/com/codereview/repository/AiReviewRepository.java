package com.codereview.repository;

import com.codereview.domain.AiReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiReviewRepository extends JpaRepository<AiReview, Long> {
}
