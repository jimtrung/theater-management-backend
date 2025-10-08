package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.model.Country;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;

@Repository
public class CountryDAO {
    private final DataSource dataSource;

    public CountryDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Country country) {
        String sql = """
            INSERT INTO countries (code, name, iso3, phone_code, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, country.getCode());
            ps.setString(2, country.getName());
            ps.setString(3, country.getIso3());
            ps.setString(4, country.getPhoneCode());
            ps.setObject(5, country.getCreatedAt());
            ps.setObject(6, country.getUpdatedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert new country", e);
        }
    }

    public Country getByField(String fieldName, Object fieldValue) {
        String sql = """
        SELECT code, name, iso3, phone_code, created_at, updated_at
        FROM countries
        WHERE """ + fieldName + " = ? LIMIT 1;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Country(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("iso3"),
                    rs.getString("phone_code"),
                    rs.getObject("created_at", OffsetDateTime.class),
                    rs.getObject("updated_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get country", e);
        }

        return null;
    }

    public void updateByField(String code, String fieldName, Object fieldValue) {
        String sql = "UPDATE countries SET " + fieldName + " = ? WHERE code = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, fieldValue);
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update country", e);
        }
    }

    public void delete(String code) {
        String sql = "DELETE FROM countries WHERE code = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete country with code: " + code, e);
        }
    }
}
