package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class UserDAO {
  /* Attribute */
  private final Connection conn;

  /* Constructor */
  public UserDAO(Connection conn) {
    this.conn = conn;
  }

  /* Functions */
  public void insert(User user) throws Exception {
    String sql = """
        INSERT INTO users (username, email, phone_number, password, role, provider, token, otp, verified)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPhoneNumber());
      ps.setString(4, user.getPassword());
      ps.setObject(5, user.getRole(), Types.OTHER);
      ps.setObject(6, user.getProvider(), Types.OTHER);
      ps.setString(7, user.getToken());
      ps.setInt(8, user.getOtp());
      ps.setBoolean(9, user.getVerified());
      ps.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException("Failed to insert new user: " + e.toString());
    }
  }

  public User getByField(String fieldName, Object value) throws Exception {
    String sql = """
        SELECT id, username, email, phone_number, password, role, provider, token, otp, verified, created_at, updated_at
        FROM users
        WHERE\s""" + fieldName + " = ?;";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new User(
            (UUID) rs.getObject("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getString("password"),
            UserRole.valueOf(rs.getString("role")),
            Provider.valueOf(rs.getString("provider")),
            rs.getString("token"),
            rs.getInt("otp"),
            rs.getBoolean("verified"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
        );
      }

    } catch (SQLException e) {
      throw new RuntimeException("Failed to fetch user by " + fieldName + ": " + e);
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws Exception {
    String sql = "UPDATE users SET " + fieldName + " = ? WHERE id = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update user by " + fieldName + ": " + e);
    }
  }

  public void delete(UUID id) throws Exception {
    String sql = """
            DELETE FROM users
            WHERE id = ?;
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete user with the id: " + id, e);
    }
  }
}
