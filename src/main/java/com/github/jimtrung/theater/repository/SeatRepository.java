package com.github.jimtrung.theater.repository;

import com.github.jimtrung.theater.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByAuditoriumId(UUID auditoriumId);
    void deleteByAuditoriumId(UUID auditoriumId);
}
