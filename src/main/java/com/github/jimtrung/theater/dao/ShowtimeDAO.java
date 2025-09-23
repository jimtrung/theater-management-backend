package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Showtime;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ShowtimeDAO {
  private final Connection conn;

  public ShowtimeDAO(Connection conn) {
    this.conn = conn;
  }

  public void insert(Showtime showtime) throws SQLException {
    String sql = """
            INSERT INTO showtimes (id, movie_id, auditorium_id, start_time, end_time, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, showtime.getId());
      ps.setObject(2, showtime.getMovieId());
      ps.setObject(3, showtime.getAuditoriumId());
      ps.setObject(4, showtime.getStartTime());
      ps.setObject(5, showtime.getEndTime());
      ps.setObject(6, showtime.getCreatedAt());
      ps.setObject(7, showtime.getUpdatedAt());
      ps.executeUpdate();
    }
  }

  public Showtime getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM showtimes WHERE " + fieldName + " = ? LIMIT 1";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
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
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE showtimes SET " + fieldName + " = ? WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    }
  }

  public void delete(UUID id) throws SQLException {
    String sql = "DELETE FROM showtimes WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
}
