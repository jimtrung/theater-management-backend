package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Movie;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MovieDAO {
    private final DataSource dataSource;

    public MovieDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Movie movie) {
        String sql = """
            INSERT INTO movies (name, author, description, genres, duration, ageLimit)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, movie.getName());
            ps.setString(2, movie.getAuthor());
            ps.setString(3, movie.getDescription());
            ps.setString(4, movie.getGenres());
            ps.setInt(5, movie.getDuration());
            ps.setInt(6, movie.getAgeLimit());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert a movie", e);
        }
    }

    public List<Movie> getAllMovies() {
        String sql = "SELECT id, name, author, description, genres, duration, ageLimit FROM movies";
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getObject("id", UUID.class));
                movie.setName(rs.getString("name"));
                movie.setAuthor(rs.getString("author"));
                movie.setDescription(rs.getString("description"));
                movie.setGenres(rs.getString("genres"));
                movie.setDuration(rs.getInt("duration"));
                movie.setAgeLimit(rs.getInt("ageLimit"));

                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get all movies", e);
        }

        return movies;
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM movies WHERE id = ?;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete movie with id: " + id, e);
        }
    }

    public void deleteAllMovies() {
        String sql = "DELETE FROM movies";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete all movies", e);
        }
    }
}
