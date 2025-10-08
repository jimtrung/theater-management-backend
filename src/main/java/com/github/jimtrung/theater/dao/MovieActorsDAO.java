package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.MovieActors;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class MovieActorsDAO {
    private final DataSource dataSource;

    public MovieActorsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(MovieActors ma) {
        String sql = """
            INSERT INTO movie_actors (movie_id, actor_id, created_at, updated_at)
            VALUES (?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, ma.getMovieId());
            ps.setObject(2, ma.getActorId());
            ps.setObject(3, ma.getCreatedAt());
            ps.setObject(4, ma.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new movie actors", e);
        }
    }

    public MovieActors getByField(String fieldName, Object value) {
        String sql = "SELECT movie_id, actor_id, created_at, updated_at FROM movie_actors WHERE " + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MovieActors(
                        (UUID) rs.getObject("movie_id"),
                        (UUID) rs.getObject("actor_id"),
                        rs.getObject("created_at", OffsetDateTime.class),
                        rs.getObject("updated_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get movie actors", e);
        }

        return null;
    }

    public void updateByField(UUID movieId, UUID actorId, String fieldName, Object fieldValue) {
        String sql = "UPDATE movie_actors SET " + fieldName + " = ? WHERE movie_id = ? AND actor_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setObject(2, movieId);
            ps.setObject(3, actorId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update movie actors", e);
        }
    }

    public void delete(UUID movieId, UUID actorId) {
        String sql = "DELETE FROM movie_actors WHERE movie_id = ? AND actor_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, movieId);
            ps.setObject(2, actorId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete movie actors", e);
        }
    }
}
