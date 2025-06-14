CREATE TABLE blacklist (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    user_black_id UUID NOT NULL,
    created_at TIMESTAMP,
    UNIQUE (user_id, user_black_id)
);