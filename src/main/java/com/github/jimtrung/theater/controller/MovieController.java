package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable UUID id) {
        try {
            Movie movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phim với ID: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<String> insertMovie(@RequestBody Movie movie) {
        try {
            movieService.insertMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm phim thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Thêm phim thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllMovies() {
        try {
            movieService.deleteAllMovies();
            return ResponseEntity.ok("Tất cả phim đã được xóa thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa tất cả phim thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieById(@PathVariable UUID id) {
        try {
            movieService.deleteMovieById(id);
            return ResponseEntity.ok("Xóa phim thành công với ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa phim thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovieById(@PathVariable UUID id, @RequestBody Movie movie) {
        try {
            movieService.updateMovie(id, movie);
            return ResponseEntity.ok("Cập nhật phim thành công với ID: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cập nhật phim thất bại: " + e.getMessage());
        }
    }
}
