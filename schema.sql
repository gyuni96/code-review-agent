
-- Database Schema Creation Script

-- 1. Users Table
CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
github_id BIGINT UNIQUE NOT NULL,
username VARCHAR(255) NOT NULL,
email VARCHAR(255),
avatar_url TEXT,
access_token TEXT NOT NULL, -- Should be stored encrypted at application level
refresh_token TEXT, -- Should be stored encrypted at application level
created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Repositories Table
CREATE TABLE repositories (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
github_repo_id BIGINT UNIQUE NOT NULL,
full_name VARCHAR(255) NOT NULL,
is_active BOOLEAN DEFAULT FALSE,
webhook_id VARCHAR(255),
created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Pull Requests Table
CREATE TABLE pull_requests (
id BIGSERIAL PRIMARY KEY,
repo_id BIGINT NOT NULL REFERENCES repositories(id) ON DELETE CASCADE,
github_pr_id BIGINT UNIQUE NOT NULL,
pr_number INTEGER NOT NULL,
title TEXT,
author VARCHAR(255),
status VARCHAR(50), -- OPEN, CLOSED, MERGED
created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. AI Reviews Table
CREATE TABLE ai_reviews (
id BIGSERIAL PRIMARY KEY,
pr_id BIGINT NOT NULL REFERENCES pull_requests(id) ON DELETE CASCADE,
status VARCHAR(50) NOT NULL, -- PENDING, IN_PROGRESS, COMPLETED, FAILED
raw_analysis_result JSONB,
github_comment_id VARCHAR(255),
created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
completed_at TIMESTAMP WITH TIME ZONE
);

-- Indexes for performance
CREATE INDEX idx_repositories_user_id ON repositories(user_id);
CREATE INDEX idx_pull_requests_repo_id ON pull_requests(repo_id);
CREATE INDEX idx_ai_reviews_pr_id ON ai_reviews(pr_id);
CREATE INDEX idx_ai_reviews_status ON ai_reviews(status);
