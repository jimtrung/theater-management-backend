package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class UserDAO {
    private final DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(User user) {
        String sql = """
            INSERT INTO users (username, email, phone_number, password, role, provider, token, otp, verified)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new user", e);
        }
    }

    public User getByField(String fieldName, Object fieldValue) {
        String sql = "SELECT * FROM users WHERE " + fieldName + " = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
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
            throw new DatabaseOperationException("Failed to fetch user by field: " + fieldName, e);
        }

        return null;
    }

    public void updateByField(UUID id, String fieldName, Object value) {
        String sql = "UPDATE users SET " + fieldName + " = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, value);
            ps.setObject(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update user field: " + fieldName, e);
        }
    }

    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete user with ID: " + id, e);
        }
    }

    public User getByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber) {
        String sql = """
            SELECT id, username, email, phone_number, password, role, provider, otp, token, verified, created_at, updated_at
            FROM users
            WHERE username = ? OR email = ? OR phone_number = ?
            LIMIT 1;
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
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
            throw new DatabaseOperationException("Failed to fetch user by username/email/phone number", e);
        }

        return null;
    }
}
