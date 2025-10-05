package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Seat;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class SeatDAO {
  private final DataSource dataSource;

  public SeatDAO(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void insert(Seat seat) throws SQLException {
    String sql = """
      INSERT INTO seats (id, auditorium_id, row, number, created_at, updated_at)
      VALUES (?, ?, ?, ?, ?, ?);
      """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, seat.getId());
      ps.setObject(2, seat.getAuditoriumId());
      ps.setString(3, seat.getRow());
      ps.setInt(4, seat.getNumber());
      ps.setObject(5, seat.getCreatedAt());
      ps.setObject(6, seat.getUpdatedAt());

      ps.executeUpdate();
    }
  }

  public Seat getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM seats WHERE " + fieldName + " = ? LIMIT 1";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new Seat(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("auditorium_id"),
            rs.getString("row"),
            rs.getInt("number"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
        );
      }
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE seats SET " + fieldName + " = ? WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    }
  }

  public void delete(UUID id) throws SQLException {
    String sql = "DELETE FROM seats WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
}
