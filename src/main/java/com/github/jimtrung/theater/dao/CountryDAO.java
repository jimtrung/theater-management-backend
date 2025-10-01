package com.github.jimtrung.theater.dao;

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

  // --- Create ---
  public void insert(Country country) throws SQLException {
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
    }
  }

  // --- Read single record by field ---
  public Country getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM countries WHERE " + fieldName + " = ? LIMIT 1";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
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
    }

    return null; // no record found
  }

  // --- Update single field ---
  public void updateByField(String code, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE countries SET " + fieldName + " = ? WHERE code = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ps.setString(2, code);
      ps.executeUpdate();
    }
  }

  // --- Delete ---
  public void delete(String code) throws SQLException {
    String sql = "DELETE FROM countries WHERE code = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, code);
      ps.executeUpdate();
    }
  }
}
