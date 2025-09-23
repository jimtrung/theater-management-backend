package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Actor;
import com.github.jimtrung.theater.model.Gender;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ActorDAO {
  private final Connection conn;

  public ActorDAO(Connection conn) {
    this.conn = conn;
  }

  // --- Create ---
  public void insert(Actor actor) throws SQLException {
    String sql = """
            INSERT INTO actors (id, first_name, last_name, dob, age, gender, country_code, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
    }
  }

  // --- Read ---
  public Actor getById(UUID id) throws SQLException {
    String sql = "SELECT * FROM actors WHERE id = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
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
    }
    return null;
  }

  // --- Update ---
  public void updateByField(UUID id, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE actors SET " + fieldName + " = ? WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    }
  }

  // --- Delete ---
  public void delete(UUID id) throws SQLException {
    String sql = "DELETE FROM actors WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
}
