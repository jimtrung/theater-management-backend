package com.github.jimtrung.theater.repository;

import com.github.jimtrung.theater.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByUserId(UUID userId);
    List<Ticket> findByShowtimeId(UUID showtimeId);
    boolean existsByShowtimeIdAndSeatId(UUID showtimeId, UUID seatId);
}
