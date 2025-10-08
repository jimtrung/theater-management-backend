package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dao.MovieDAO;
import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.service.MovieService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieDAO movieDAO;

    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieDAO.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<String> insertMovie(@RequestBody Movie movie) {
        try {
            movieDAO.insert(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie insert successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert movie: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllMovies() {
        try {
            movieDAO.deleteAllMovies();
            return ResponseEntity.ok("All movies have been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all movies: " + e.getMessage());
        }
    }
}
