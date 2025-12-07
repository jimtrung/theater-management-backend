CREATE TABLE profiles (
    user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,

    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,    
    gender gender_type NOT NULL,

    email TEXT REFERENCES users(email) ON DELETE CASCADE, 

    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
