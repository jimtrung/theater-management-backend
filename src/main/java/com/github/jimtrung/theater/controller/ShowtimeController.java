package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.Showtime;
import com.github.jimtrung.theater.service.ShowtimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        List<Showtime> showtimes = showtimeService.getAllShowtimes();
        if (showtimes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(showtimes);
    }

    @PostMapping
    public ResponseEntity<String> insertShowtime(@RequestBody Showtime showtime) {
        try {
            showtimeService.insert(showtime);
            return ResponseEntity.status(HttpStatus.CREATED).body("Showtime inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert showtime: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllShowtimes() {
        try {
            showtimeService.deleteAllShowtimes();
            return ResponseEntity.ok("All showtimes have been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all showtimes: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowtimeById(@PathVariable UUID id) {
        try {
            showtimeService.delete(id);
            return ResponseEntity.ok("Showtime has been deleted successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete showtime: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable UUID id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        if (showtime.getId() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(showtime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateShowtimeById(@PathVariable UUID id, @RequestBody Showtime showtime) {
        try {
            showtimeService.updateShowtimeById(id, showtime);
            return ResponseEntity.ok("Showtime updated successfully with id: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update showtime: " + e.getMessage());
        }
    }
    @GetMapping("/{id}/seats")
    public ResponseEntity<List<com.github.jimtrung.theater.dto.SeatStatusDTO>> getSeatsForShowtime(@PathVariable UUID id) {
        return ResponseEntity.ok(showtimeService.getSeatsWithStatus(id));
    }
}
