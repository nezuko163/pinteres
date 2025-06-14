CREATE TABLE user_follow (
    id BIGSERIAL PRIMARY KEY,
    subscriber_id UUID NOT NULL,
    followee_id UUID NOT NULL,
    created_at TIMESTAMP,
    UNIQUE (subscriber_id, followee_id)
);