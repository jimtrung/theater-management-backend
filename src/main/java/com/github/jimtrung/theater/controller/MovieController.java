package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dao.MovieDAO;
import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieDAO movieDAO;

    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // --- GET all ---
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieDAO.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    // --- GET by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable UUID id) {
        try {
            Movie movie = movieDAO.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching movie: " + e.getMessage());
        }
    }

    // --- POST (Insert) ---
    @PostMapping
    public ResponseEntity<String> insertMovie(@RequestBody Movie movie) {
        try {
            movieDAO.insert(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie inserted successfully 🎬");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert movie: " + e.getMessage());
        }
    }

    // --- DELETE ALL ---
    @DeleteMapping
    public ResponseEntity<String> deleteAllMovies() {
        try {
            movieDAO.deleteAllMovies();
            return ResponseEntity.ok("All movies have been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all movies: " + e.getMessage());
        }
    }

    // --- DELETE by ID ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieById(@PathVariable UUID id) {
        try {
            movieDAO.delete(id);
            return ResponseEntity.ok("Movie deleted successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete movie: " + e.getMessage());
        }
    }

    // --- UPDATE by ID ---
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovieById(@PathVariable UUID id, @RequestBody Movie movie) {
        try {
            movieDAO.updateMovieById(id, movie);
            return ResponseEntity.ok("Movie updated successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update movie: " + e.getMessage());
        }
    }
}
