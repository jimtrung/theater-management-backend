package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.Auditorium;
import com.github.jimtrung.theater.service.AuditoriumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auditoriums")
public class AuditoriumController {
    private final AuditoriumService auditoriumService;

    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping
    public ResponseEntity<List<Auditorium>> getAllAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.getAllAuditoriums();
        if (auditoriums.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditoriums);
    }

    @PostMapping
    public ResponseEntity<String> insertAuditorium(@RequestBody Auditorium auditorium) {
        try {
            auditoriumService.insert(auditorium);
            return ResponseEntity.status(HttpStatus.CREATED).body("Auditorium insert successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert auditorium: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllAuditoriums() {
        try {
            auditoriumService.deleteAllAuditoriums();
            return ResponseEntity.ok("All auditoriums have been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all auditoriums: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuditoriumById(@PathVariable UUID id) {
        try {
            auditoriumService.delete(id);
            return ResponseEntity.ok("Auditorium have been deleted successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete auditorium: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditorium> getAuditoriumById(@PathVariable UUID id) {
        Auditorium auditorium = auditoriumService.getAuditoriumById(id);
        if (auditorium.getId() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditorium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuditoriumById(@PathVariable UUID id, @RequestBody Auditorium auditorium) {
        try {
            auditoriumService.updateAuditoriumById(id, auditorium);
            return ResponseEntity.ok("Auditorium updated successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update auditorium: " + e.getMessage());
        }
    }
}
