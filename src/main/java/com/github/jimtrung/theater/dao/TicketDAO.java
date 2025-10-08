package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Ticket;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class TicketDAO {
    private final DataSource dataSource;

    public TicketDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Ticket ticket) throws SQLException {
        String sql = """
            INSERT INTO tickets (id, user_id, showtime_id, seat_id, price, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, ticket.getId());
            ps.setObject(2, ticket.getUserId());
            ps.setObject(3, ticket.getShowtimeId());
            ps.setObject(4, ticket.getSeatId());
            ps.setInt(5, ticket.getPrice());
            ps.setObject(6, ticket.getCreatedAt());
            ps.setObject(7, ticket.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new ticket", e);
        }
    }

    public Ticket getByField(String fieldName, Object fieldValue) throws SQLException {
        String sql = """
            SELECT id, user_id, showtime_id, seat_id, price, created_at, updated_at
            FROM tickets
            WHERE """ + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
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

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get ticket by field: " + fieldName, e);
        }

        return null;
    }

    public void updateByField(UUID id, String fieldName, Object fieldValue) throws SQLException {
        String sql = "UPDATE tickets SET " + fieldName + " = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setObject(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update ticket field: " + fieldName, e);
        }
    }

    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM tickets WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete ticket with ID: " + id, e);
        }
    }
}
