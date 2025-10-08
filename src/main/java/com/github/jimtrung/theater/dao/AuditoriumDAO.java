package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Auditorium;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class AuditoriumDAO {
    private final DataSource dataSource;

    public AuditoriumDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Auditorium auditorium) {
        String sql = """
            INSERT INTO auditoriums (id, capacity, created_at, updated_at)
            VALUES (?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, auditorium.getId());
            ps.setInt(2, auditorium.getCapacity());
            ps.setObject(3, auditorium.getCreatedAt());
            ps.setObject(4, auditorium.getUpdatedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new auditorium", e);
        }
    }

    public Auditorium getByField(String fieldName, Object fieldValue) {
        String sql = "SELECT id, capacity, created_at, updated_at FROM auditoriums WHERE " + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Auditorium(
                    (UUID) rs.getObject("id"),
                    rs.getInt("capacity"),
                    rs.getObject("created_at", OffsetDateTime.class),
                    rs.getObject("updated_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get auditorium", e);
        }

        return null;
    }

    public void updateByField(UUID id, String fieldName, Object fieldValue) {
        String sql = "UPDATE auditoriums SET " + fieldName + " = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update auditorium", e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM auditoriums WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete auditorium with id: " + id, e);
        }
    }
}
