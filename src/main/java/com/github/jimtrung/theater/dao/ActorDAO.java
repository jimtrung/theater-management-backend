package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Actor;
import com.github.jimtrung.theater.model.Gender;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ActorDAO {
    private final javax.sql.DataSource dataSource;

    public ActorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Actor actor) {
        String sql = """
            INSERT INTO actors (id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, actor.getId());
            ps.setString(2, actor.getFirstName());
            ps.setString(3, actor.getLastName());
            ps.setDate(4, new java.sql.Date(actor.getDob().getTime()));
            ps.setInt(5, actor.getAge());
            ps.setObject(6, actor.getGender(), Types.OTHER);
            ps.setString(7, actor.getCountryCode());
            ps.setObject(8, actor.getCreatedAt());
            ps.setObject(9, actor.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new actor", e);
        }
    }

    public Actor getByField(String fieldName, Object fieldValue) {
        String sql = """
            SELECT id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at FROM actors 
            WHERE """ + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Actor(
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
            throw new DatabaseOperationException("Failed to get actor", e);
        }
        return null;
    }

    public List<Actor> getAll() {
        String sql = """
        SELECT id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at 
        FROM actors
        ORDER BY created_at DESC;
        """;

        List<Actor> actors = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Actor actor = new Actor(
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
                actors.add(actor);
            }

            System.out.println("[DEBUG] - getAllActors - Fetched " + actors.size() + " actors from DB");

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch all actors", e);
        }

        return actors;
    }

    public void updateByField(UUID id, String fieldName, Object value) {
        String sql = "UPDATE actors SET " + fieldName + " = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, value);
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update actor with id: " + id, e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM actors WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete actor with id: " + id, e);
        }
    }
}
