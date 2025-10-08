package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Director;
import com.github.jimtrung.theater.model.Gender;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class DirectorDAO {
    private final DataSource dataSource;

    public DirectorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Director director) {
        String sql = """
            INSERT INTO directors (id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, director.getId());
            ps.setString(2, director.getFirstName());
            ps.setString(3, director.getLastName());
            ps.setDate(4, new java.sql.Date(director.getDob().getTime()));
            ps.setInt(5, director.getAge());
            ps.setObject(6, director.getGender(), Types.OTHER);
            ps.setString(7, director.getCountryCode());
            ps.setObject(8, director.getCreatedAt());
            ps.setObject(9, director.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new actor", e);
        }
    }

    public Director getByField(String fieldName, Object fieldValue) {
        String sql = """
            SELECT id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at
            FROM directors
            WHERE """ + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Director(
                    (UUID) rs.getObject("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("dob"),
                    rs.getInt("age"),
                    Gender.valueOf(rs.getString("gender")),
                    rs.getString("country_code"),
                    rs.getObject("created_at", OffsetDateTime.class),
                    rs.getObject("updated_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get director", e);
        }

        return null;
    }

    public void updateByField(UUID id, String fieldName, Object fieldValue) {
        String sql = "UPDATE directors SET " + fieldName + " = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update director", e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM directors WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete director with id: " + id, e);
        }
    }
}
