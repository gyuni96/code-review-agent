package com.codereview.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewJob implements Serializable {
    private String owner;
    private String repo;
    private int prNumber;
    private Long pullRequestId;
    private String accessToken;
}
