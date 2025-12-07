-- Movies
CREATE TRIGGER set_updated_at_movies
BEFORE UPDATE ON movies
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
-- Auditoriums
CREATE TRIGGER set_updated_at_auditoriums
BEFORE UPDATE ON auditoriums
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Seats
CREATE TRIGGER set_updated_at_seats
BEFORE UPDATE ON seats
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Showtimes
CREATE TRIGGER set_updated_at_showtimes
BEFORE UPDATE ON showtimes
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Tickets
CREATE TRIGGER set_updated_at_tickets
BEFORE UPDATE ON tickets
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Users
CREATE TRIGGER set_updated_at_users
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Profiles
CREATE TRIGGER set_updated_at_profiles
BEFORE UPDATE ON profiles
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
