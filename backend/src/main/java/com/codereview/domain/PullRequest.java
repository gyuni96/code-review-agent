package com.codereview.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pull_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id", nullable = false)
    private Repository repository;

    @Column(name = "github_pr_id", unique = true, nullable = false)
    private Long githubPrId;

    @Column(name = "pr_number", nullable = false)
    private Integer prNumber;

    private String title;

    private String author;

    private String status; // OPEN, CLOSED, MERGED

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "pullRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AiReview> aiReviews = new ArrayList<>();
}
