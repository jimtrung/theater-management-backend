CREATE TABLE movies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    description TEXT,
    director TEXT,
    actors TEXT[],
    genres movie_genre[] NOT NULL,
    premiere TIMESTAMPTZ,
    duration INT,
    language movie_language,
    rated INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
