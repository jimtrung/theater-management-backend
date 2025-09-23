package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.model.MovieGenre;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.UserRole;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MovieDAO {
  public final Connection conn;

  public MovieDAO(Connection conn) {
    this.conn = conn;
  }

  public void insert(Movie movie) throws Exception {
    String sql = """
        INSERT INTO movies (name, description, director_id, genres, premiere, duration, language, rated)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, movie.getName());
      ps.setString(2, movie.getDescription());
      ps.setObject(3, movie.getDirectorId());
      ps.setObject(4, movie.getGenres(), Types.OTHER);
      ps.setObject(5, movie.getPremiere());
      ps.setInt(6, movie.getDuration());
      ps.setString(7, movie.getLanguage());
      ps.setInt(8,  movie.getRated());
      ps.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException("Failed to insert a movie: " + e);
    }
  }

  public Movie getByField(String fieldName, Object value) throws Exception {
    String sql = """
        SELECT id, name, description, director_id, genres, premiere, duration, language, rated, created_at, updated_at
        FROM movies
        WHERE\s""" + fieldName + " = ?;";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new Movie(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("director_id"),
            rs.getString("name"),
            rs.getString("description"),
            (MovieGenre[]) rs.getObject("genres"), // Need to fix this later on
            (OffsetDateTime) rs.getObject("premiere"),
            rs.getInt("duration"),
            rs.getString("language"),
            rs.getInt("rated"),
            (OffsetDateTime) rs.getObject("created_at"),
            (OffsetDateTime) rs.getObject("updated_at")
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to fetch movie by " + fieldName + ": " + e);
    }

    return null;
  }

  public void updateByField(UUID id, String fieldName, Object value) throws Exception {
    String sql = "UPDATE movies SET " + fieldName + " = ? WHERE id = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, value);
      ps.setObject(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update movies by " + fieldName + ": " + e);
    }
  }

  public void delete(UUID id) throws Exception {
    String sql = """
            DELETE FROM movies 
            WHERE id = ?;
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete movie with the id: " + id, e);
    }
  }
}
