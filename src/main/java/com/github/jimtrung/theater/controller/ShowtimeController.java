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
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm suất chiếu thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Thêm suất chiếu thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllShowtimes() {
        try {
            showtimeService.deleteAllShowtimes();
            return ResponseEntity.ok("Tất cả suất chiếu đã được xóa thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa tất cả suất chiếu thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowtimeById(@PathVariable UUID id) {
        try {
            showtimeService.delete(id);
            return ResponseEntity.ok("Xóa suất chiếu thành công với ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa suất chiếu thất bại: " + e.getMessage());
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
            return ResponseEntity.ok("Cập nhật suất chiếu thành công với ID: " + id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cập nhật suất chiếu thất bại: " + e.getMessage());
        }
    }
    @GetMapping("/{id}/seats")
    public ResponseEntity<List<com.github.jimtrung.theater.dto.SeatStatusDTO>> getSeatsForShowtime(@PathVariable UUID id) {
        return ResponseEntity.ok(showtimeService.getSeatsWithStatus(id));
    }
}
