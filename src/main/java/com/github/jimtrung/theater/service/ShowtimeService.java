package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.SeatStatusDTO;
import com.github.jimtrung.theater.model.Seat;
import com.github.jimtrung.theater.model.Showtime;
import com.github.jimtrung.theater.model.Ticket;
import com.github.jimtrung.theater.repository.SeatRepository;
import com.github.jimtrung.theater.repository.ShowtimeRepository;
import com.github.jimtrung.theater.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatRepository seatRepository, TicketRepository ticketRepository) {
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public void insert(Showtime showtime) {
        showtime.setCreatedAt(OffsetDateTime.now());
        showtime.setUpdatedAt(OffsetDateTime.now());
        showtimeRepository.save(showtime);
    }

    public void deleteAllShowtimes() {
        showtimeRepository.deleteAll();
    }

    public void delete(UUID id) {
        showtimeRepository.deleteById(id);
    }

    public Showtime getShowtimeById(UUID id) {
        return showtimeRepository.findById(id).orElse(new Showtime());
    }

    public void updateShowtimeById(UUID id, Showtime showtime) {
        showtime.setId(id);
        showtime.setUpdatedAt(OffsetDateTime.now());
        showtimeRepository.save(showtime);
    }

    public List<SeatStatusDTO> getSeatsWithStatus(UUID showtimeId) {
        Showtime showtime = getShowtimeById(showtimeId);
        if (showtime.getId() == null) {
            return List.of();
        }

        List<Seat> seats = seatRepository.findByAuditoriumId(showtime.getAuditoriumId());
        List<Ticket> tickets = ticketRepository.findByShowtimeId(showtimeId);
        Set<UUID> bookedSeatIds = tickets.stream()
                .map(Ticket::getSeatId)
                .collect(Collectors.toSet());

        return seats.stream()
                .map(seat -> new SeatStatusDTO(seat, bookedSeatIds.contains(seat.getId())))
                .collect(Collectors.toList());
    }
}
