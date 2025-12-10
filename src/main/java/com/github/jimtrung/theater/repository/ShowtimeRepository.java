package com.github.jimtrung.theater.repository;

import com.github.jimtrung.theater.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {
    List<Showtime> findByMovieId(UUID movieId);
    List<Showtime> findByAuditoriumId(UUID auditoriumId);
    void deleteByEndTimeBefore(OffsetDateTime dateTime);
}
