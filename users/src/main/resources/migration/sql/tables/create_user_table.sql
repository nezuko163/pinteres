CREATE TABLE user_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username TEXT NOT NULL,
    avatar_id TEXT,
    bio TEXT,
    country TEXT,
    birth_date DATE,
    gender TEXT,
    language TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);