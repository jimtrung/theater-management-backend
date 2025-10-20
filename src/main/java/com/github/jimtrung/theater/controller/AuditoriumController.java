package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dao.AuditoriumDAO;
import com.github.jimtrung.theater.dao.MovieDAO;
import com.github.jimtrung.theater.model.Auditorium;
import com.github.jimtrung.theater.model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auditoriums")
public class AuditoriumController {
    private final AuditoriumDAO auditoriumDAO;

    public AuditoriumController(AuditoriumDAO auditoriumDAO) {
        this.auditoriumDAO = auditoriumDAO;
    }

    @GetMapping
    public ResponseEntity<List<Auditorium>> getAllAuditoriums() {
        List<Auditorium> auditoriums = auditoriumDAO.getAllAuditoriums();
        if (auditoriums.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditoriums);
    }

    @PostMapping
    public ResponseEntity<String> insertAuditorium(@RequestBody Auditorium auditorium) {
        try {
            auditoriumDAO.insert(auditorium);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie insert successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert movie: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllAuditoriums() {
        try {
            auditoriumDAO.deleteAllAuditoriums();
            return ResponseEntity.ok("All movies have been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all movies: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuditoriumById(@PathVariable UUID id) {
        try {
            auditoriumDAO.delete(id);
            return ResponseEntity.ok("Movie have been deleted successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all movies: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditorium> getAuditoriumById(@PathVariable UUID id) {
        Auditorium auditorium = auditoriumDAO.getAuditoriumById(id);
        if (auditorium.getId() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditorium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuditoriumById(@PathVariable UUID id, @RequestBody Auditorium auditorium) {
        try {
            auditoriumDAO.updateAuditoriumById(id, auditorium);
            return ResponseEntity.ok("Movie updated successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update movie: " + e.getMessage());
        }
    }
}
