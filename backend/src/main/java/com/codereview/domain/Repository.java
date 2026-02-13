package com.codereview.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "github_repo_id", unique = true, nullable = false)
    private Long githubRepoId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = false;

    @Column(name = "webhook_id")
    private String webhookId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PullRequest> pullRequests = new ArrayList<>();
}
