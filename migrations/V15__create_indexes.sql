CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_token ON users (token); 

CREATE INDEX IF NOT EXISTS idx_movies_name ON movies (name);
CREATE INDEX IF NOT EXISTS idx_movies_genres ON movies USING GIN (genres);
CREATE INDEX IF NOT EXISTS idx_movies_director ON movies (director);

CREATE INDEX IF NOT EXISTS idx_showtimes_movie_id ON showtimes (movie_id);
CREATE INDEX IF NOT EXISTS idx_showtimes_auditorium_id ON showtimes (auditorium_id);
CREATE INDEX IF NOT EXISTS idx_showtimes_start_time ON showtimes (start_time);

CREATE INDEX IF NOT EXISTS idx_tickets_user_id ON tickets (user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_showtime_id ON tickets (showtime_id);

CREATE INDEX IF NOT EXISTS idx_seats_auditorium_id ON seats (auditorium_id);

CREATE INDEX IF NOT EXISTS idx_profiles_full_name ON profiles (full_name);
