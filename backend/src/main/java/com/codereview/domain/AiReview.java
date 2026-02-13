package com.codereview.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "ai_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", nullable = false)
    private PullRequest pullRequest;

    @Column(nullable = false)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, FAILED

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_analysis_result", columnDefinition = "jsonb")
    private Map<String, Object> rawAnalysisResult;

    @Column(name = "github_comment_id")
    private String githubCommentId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
