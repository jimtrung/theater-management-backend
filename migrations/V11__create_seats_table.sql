CREATE TABLE seats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    auditorium_id UUID NOT NULL REFERENCES auditoriums(id) ON DELETE CASCADE,
    row TEXT NOT NULL,
    number INT NOT NULL,
    is_booked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
