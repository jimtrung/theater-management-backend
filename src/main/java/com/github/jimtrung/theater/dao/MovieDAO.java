package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Movie;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;

@Repository
public class MovieDAO {
    private final DataSource dataSource;

    public MovieDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // --- INSERT ---
    public void insert(Movie movie) {
        String sql = """
            INSERT INTO movies (id, name, description, director_id, genres, premiere, duration, language, rated)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // 1. id
            ps.setObject(1, movie.getId());

            // 2. name
            ps.setString(2, movie.getName());

            // 3. description
            ps.setString(3, movie.getDescription());

            // 4. director_id
            ps.setObject(4, movie.getDirectorId());

            // 5. genres → convert List<String> to String[]
            Array genreArray = connection.createArrayOf("movie_genre", movie.getGenres().toArray(String[]::new));
            ps.setArray(5, genreArray);

            // 6. premiere
            if (movie.getPremiere() != null)
                ps.setObject(6, Timestamp.from(movie.getPremiere().toInstant()));
            else
                ps.setNull(6, Types.TIMESTAMP_WITH_TIMEZONE);

            // 7. duration
            if (movie.getDuration() != null)
                ps.setInt(7, movie.getDuration());
            else
                ps.setNull(7, Types.INTEGER);

            // 8. language
            ps.setString(8, movie.getLanguage());

            // 9. rated
            if (movie.getRated() != null)
                ps.setInt(9, movie.getRated());
            else
                ps.setNull(9, Types.INTEGER);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to insert movie");
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to insert movie", e);
        }
    }

    // --- GET ALL ---
    public List<Movie> getAllMovies() {
        String sql = """
                SELECT id, name, description, director_id, genres, premiere, duration, language, rated,
                       created_at, updated_at
                FROM movies
                """;
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie movie = mapResultSetToMovie(rs);
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get all movies");
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to get all movies", e);
        }

        return movies;
    }

    // --- GET BY ID ---
    public Movie getMovieById(UUID id) {
        String sql = """
                SELECT id, name, description, director_id, genres, premiere, duration, language, rated,
                       created_at, updated_at
                FROM movies WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToMovie(rs);
            } else {
                throw new DatabaseOperationException("Movie not found with id: " + id, null);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get movie by id: " + id);
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to get movie by id", e);
        }
    }

    // --- DELETE ---
    public void delete(UUID id) {
        String sql = "DELETE FROM movies WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to delete movie with id: " + id);
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to delete movie", e);
        }
    }

    public void deleteAllMovies() {
        String sql = "DELETE FROM movies";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to delete all movies");
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to delete all movies", e);
        }
    }

    // --- UPDATE ---
    public void updateMovieById(UUID id, Movie movie) {
        String sql = """
                UPDATE movies
                SET name = ?, description = ?, director_id = ?, genres = ?, premiere = ?, duration = ?, language = ?, rated = ?
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setObject(3, movie.getDirectorId());

            Array genreArray = connection.createArrayOf("movie_genre", movie.getGenres().toArray());
            ps.setArray(4, genreArray);

            if (movie.getPremiere() != null)
                ps.setObject(5, Timestamp.from(movie.getPremiere().toInstant()));
            else
                ps.setNull(5, Types.TIMESTAMP_WITH_TIMEZONE);

            ps.setObject(6, movie.getDuration());
            ps.setString(7, movie.getLanguage());
            ps.setObject(8, movie.getRated());
            ps.setObject(9, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update movie with id: " + id);
            System.err.println("Cause: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to update movie", e);
        }
    }

    // --- Helper to map result ---
    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getObject("id", UUID.class));
        movie.setName(rs.getString("name"));
        movie.setDescription(rs.getString("description"));
        movie.setDirectorId(rs.getObject("director_id", UUID.class));

        Array genresArray = rs.getArray("genres");
        if (genresArray != null) {
            movie.setGenres(Arrays.asList((String[]) genresArray.getArray()));
        }

        Timestamp premiereTs = rs.getTimestamp("premiere");
        if (premiereTs != null)
            movie.setPremiere(premiereTs.toInstant().atOffset(OffsetDateTime.now().getOffset()));

        movie.setDuration(rs.getInt("duration"));
        movie.setLanguage(rs.getString("language"));
        movie.setRated(rs.getInt("rated"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null)
            movie.setCreatedAt(createdAt.toInstant().atOffset(OffsetDateTime.now().getOffset()));
        if (updatedAt != null)
            movie.setUpdatedAt(updatedAt.toInstant().atOffset(OffsetDateTime.now().getOffset()));

        return movie;
    }
}
