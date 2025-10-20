package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Auditorium;
import com.github.jimtrung.theater.model.Movie;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class AuditoriumDAO {
    private final DataSource dataSource;

    public AuditoriumDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Auditorium auditorium) {
        String sql = """
            INSERT INTO auditoriums (name, type, capacity, note)
            VALUES (?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auditorium.getName());
            ps.setString(2, auditorium.getType());
            ps.setInt(3, auditorium.getCapacity());
            ps.setString(4, auditorium.getNote());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new auditorium", e);
        }
    }

    public List<Auditorium> getAllAuditoriums() {
        String sql = "SELECT id, name, type, capacity, note FROM auditoriums";
        List<Auditorium> auditoriums = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Auditorium auditorium = new Auditorium();
                auditorium.setId(rs.getObject("id", UUID.class));
                auditorium.setName(rs.getString("name"));
                auditorium.setType(rs.getString("type"));
                auditorium.setCapacity(rs.getInt("capacity"));
                auditorium.setNote(rs.getString("note"));

                auditoriums.add(auditorium);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get all movies", e);
        }

        return auditoriums;
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

    public void deleteAllAuditoriums() {
        String sql = "DELETE FROM auditoriums";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete all movies", e);
        }
    }

    public Auditorium getAuditoriumById(UUID id) {
        String sql = "SELECT id, name, type, capacity, note FROM auditoriums WHERE id = ?";
        Auditorium auditorium = new Auditorium();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {


            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                auditorium.setId(rs.getObject("id", UUID.class));
                auditorium.setName(rs.getString("name"));
                auditorium.setType(rs.getString("type"));
                auditorium.setCapacity(rs.getInt("capacity"));
                auditorium.setNote(rs.getString("note"));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get all movies", e);
        }

        return auditorium;
    }

    public void updateAuditoriumById(UUID id, Auditorium auditorium) {
        String sql = """
        UPDATE auditoriums
        SET name = ?, type = ?, capacity = ?, note = ?
        WHERE id = ?;
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, auditorium.getName());
            ps.setString(2, auditorium.getType());
            ps.setInt(3, auditorium.getCapacity());
            ps.setString(4, auditorium.getNote());
            ps.setObject(5, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DatabaseOperationException("No movie found with id: " + id, null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseOperationException("Failed to update movie with id: " + id, e);
        }
    }
}
