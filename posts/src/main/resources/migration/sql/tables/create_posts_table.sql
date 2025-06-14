CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id UUID NOT NULL,
    content TEXT NOT NULL,
    tags TEXT[] NOT NULL DEFAULT '{}',
    posted_at TIMESTAMP NOT NULL DEFAULT now(),
    created_at TIMESTAMP NULL DEFAULT now(),
    updated_at TIMESTAMP NULL DEFAULT now(),
    status TEXT NOT NULL DEFAULT 'UNDEFINED'
);

CREATE INDEX idx_posts_author_id ON posts(author_id);