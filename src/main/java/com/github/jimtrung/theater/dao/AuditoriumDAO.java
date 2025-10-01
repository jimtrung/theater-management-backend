package com.github.jimtrung.theater.dao;

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

  public void insert(Auditorium auditorium) throws SQLException {
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
    }
  }

  public Auditorium getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM auditoriums WHERE " + fieldName + " = ? LIMIT 1";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new Auditorium(
            (UUID) rs.getObject("id"),
            rs.getInt("capacity"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
        );
      }
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE auditoriums SET " + fieldName + " = ? WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    }
  }

  public void delete(UUID id) throws SQLException {
    String sql = "DELETE FROM auditoriums WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
}
