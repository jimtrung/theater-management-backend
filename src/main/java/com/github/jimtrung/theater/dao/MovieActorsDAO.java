package com.github.jimtrung.theater.dao;

import com.github.jimtrung.theater.model.MovieActors;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public class MovieActorsDAO {
  private final DataSource dataSource;

  public MovieActorsDAO(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void insert(MovieActors ma) throws SQLException {
    String sql = """
      INSERT INTO movie_actors (movie_id, actor_id, created_at, updated_at)
      VALUES (?, ?, ?, ?);
      """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, ma.getMovieId());
      ps.setObject(2, ma.getActorId());
      ps.setObject(3, ma.getCreatedAt());
      ps.setObject(4, ma.getUpdatedAt());

      ps.executeUpdate();
    }
  }

  public MovieActors getByField(String fieldName, Object value) throws SQLException {
    String sql = "SELECT * FROM movie_actors WHERE " + fieldName + " = ? LIMIT 1";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new MovieActors(
            (UUID) rs.getObject("movie_id"),
            (UUID) rs.getObject("actor_id"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
        );
      }
    }

    return null;
  }

  public void updateByField(UUID movieId, UUID actorId, String fieldName, Object value) throws SQLException {
    String sql = "UPDATE movie_actors SET " + fieldName + " = ? WHERE movie_id = ? AND actor_id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, value);
      ps.setObject(2, movieId);
      ps.setObject(3, actorId);

      ps.executeUpdate();
    }
  }

  public void delete(UUID movieId, UUID actorId) throws SQLException {
    String sql = "DELETE FROM movie_actors WHERE movie_id = ? AND actor_id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, movieId);
      ps.setObject(2, actorId);

      ps.executeUpdate();
    }
  }
}
