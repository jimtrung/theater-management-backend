package com.github.jimtrung.theater.repository;

import com.github.jimtrung.theater.dto.MovieRevenueDTO;
import com.github.jimtrung.theater.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByUserId(UUID userId);
    List<Ticket> findByShowtimeId(UUID showtimeId);
    boolean existsByShowtimeIdAndSeatId(UUID showtimeId, UUID seatId);

    @Query("SELECT new com.github.jimtrung.theater.dto.MovieRevenueDTO(m.id, m.name, SUM(t.price)) " +
            "FROM Ticket t, Showtime s, Movie m " +
            "WHERE t.showtimeId = s.id AND s.movieId = m.id " +
            "GROUP BY m.id, m.name")
    List<MovieRevenueDTO> getRevenuePerMovie();
}
