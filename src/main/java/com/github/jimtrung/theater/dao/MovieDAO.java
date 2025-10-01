package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.model.MovieGenre;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MovieDAO {
  private final DataSource dataSource;

  public MovieDAO(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void insert(Movie movie) {
    String sql = """
        INSERT INTO movies (id, name, description, director_id, genres, premiere, duration, language, rated)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, movie.getId());
      ps.setString(2, movie.getName());
      ps.setString(3, movie.getDescription());
      ps.setObject(4, movie.getDirectorId());
      ps.setObject(5, movie.getGenres(), Types.OTHER); // You’ll need a converter later
      ps.setObject(6, movie.getPremiere());
      ps.setInt(7, movie.getDuration());
      ps.setString(8, movie.getLanguage());
      ps.setInt(9, movie.getRated());

      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to insert a movie", e);
    }
  }

  public Movie getByField(String fieldName, Object value) {
    String sql = """
        SELECT id, name, description, director_id, genres, premiere, duration, language, rated, created_at, updated_at
        FROM movies
        WHERE """ + fieldName + " = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new Movie(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("director_id"),
            rs.getString("name"),
            rs.getString("description"),
            (MovieGenre[]) rs.getObject("genres"), // ⚠️ This won’t map automatically, needs custom handling
            (OffsetDateTime) rs.getObject("premiere"),
            rs.getInt("duration"),
            rs.getString("language"),
            rs.getInt("rated"),
            (OffsetDateTime) rs.getObject("created_at"),
            (OffsetDateTime) rs.getObject("updated_at")
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to fetch movie by " + fieldName, e);
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) {
    String sql = "UPDATE movies SET " + fieldName + " = ? WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update movies by " + fieldName, e);
    }
  }

  public void delete(UUID id) {
    String sql = "DELETE FROM movies WHERE id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete movie with id: " + id, e);
    }
  }
}
