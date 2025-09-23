package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Ticket;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TicketDAO {
  private final Connection conn;

  public TicketDAO(Connection conn) {
    this.conn = conn;
  }

  public void insert(Ticket ticket) throws SQLException {
    String sql = """
            INSERT INTO tickets (id, user_id, showtime_id, seat_id, price, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, ticket.getId());
      ps.setObject(2, ticket.getUserId());
      ps.setObject(3, ticket.getShowtimeId());
      ps.setObject(4, ticket.getSeatId());
      ps.setInt(5, ticket.getPrice());
      ps.setObject(6, ticket.getCreatedAt());
      ps.setObject(7, ticket.getUpdatedAt());
      ps.executeUpdate();
    }
  }

  public Ticket getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM tickets WHERE " + fieldName + " = ? LIMIT 1";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new Ticket(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("user_id"),
            (UUID) rs.getObject("showtime_id"),
            (UUID) rs.getObject("seat_id"),
            rs.getInt("price"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
        );
      }
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE tickets SET " + fieldName + " = ? WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    }
  }

  public void delete(UUID id) throws SQLException {
    String sql = "DELETE FROM tickets WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
}
