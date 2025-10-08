package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Showtime;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class ShowtimeDAO {
    private final DataSource dataSource;

    public ShowtimeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Showtime showtime) {
        String sql = """
            INSERT INTO showtimes (id, movie_id, auditorium_id, start_time, end_time, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, showtime.getId());
            ps.setObject(2, showtime.getMovieId());
            ps.setObject(3, showtime.getAuditoriumId());
            ps.setObject(4, showtime.getStartTime());
            ps.setObject(5, showtime.getEndTime());
            ps.setObject(6, showtime.getCreatedAt());
            ps.setObject(7, showtime.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new showtime", e);
        }
    }

    public Showtime getByField(String fieldName, Object fieldValue) {
        String sql = """
            SELECT id, movie_id, auditorium_id, created_at, updated_at, start_time, end_time
            FROM showtimes
            WHERE """ + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Showtime(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("movie_id"),
                        (UUID) rs.getObject("auditorium_id"),
                        rs.getObject("start_time", OffsetDateTime.class),
                        rs.getObject("end_time", OffsetDateTime.class),
                        rs.getObject("created_at", OffsetDateTime.class),
                        rs.getObject("updated_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get showtime", e);
        }

        return null;
    }

    public void updateByField(UUID id, String fieldName, Object fieldValue) {
        String sql = "UPDATE showtimes SET " + fieldName + " = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update showtime", e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM showtimes WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete showtime", e);
        }
    }
}
