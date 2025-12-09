package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.repository.ShowtimeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ShowtimeCleanupService {
    private final ShowtimeRepository showtimeRepository;

    public ShowtimeCleanupService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run daily at midnight
    @Transactional
    public void cleanupOldShowtimes() {
        OffsetDateTime now = OffsetDateTime.now();
        showtimeRepository.deleteByEndTimeBefore(now);
        System.out.println("Cleaned up showtimes that ended before: " + now);
    }
}
