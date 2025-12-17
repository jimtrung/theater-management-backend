package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.BookingRequest;
import com.github.jimtrung.theater.model.Ticket;
import com.github.jimtrung.theater.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.jimtrung.theater.dto.MovieRevenueDTO;
import com.github.jimtrung.theater.dto.ShowtimeRevenueDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public List<Ticket> bookTickets(UUID userId, BookingRequest request) {
        List<Ticket> tickets = new ArrayList<>();
        
        for (UUID seatId : request.seatIds()) {
            if (ticketRepository.existsByShowtimeIdAndSeatId(request.showtimeId(), seatId)) {
                // Check if existing ticket is PENDING and owned by same user? 
                // For simplicity, if it exists, it's taken. Ideally we could expire pending tickets.
                throw new RuntimeException("Ghế " + seatId + " đã được đặt.");
            }
            
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setShowtimeId(request.showtimeId());
            ticket.setSeatId(seatId);
            ticket.setPrice(50000);
            ticket.setCreatedAt(OffsetDateTime.now());
            ticket.setUpdatedAt(OffsetDateTime.now());
            ticket.setStatus("PENDING");
            
            tickets.add(ticketRepository.save(ticket));
        }
        
        return tickets;
    }

    public List<Ticket> getUserTickets(UUID userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        tickets.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return tickets;
    }

    @Transactional
    public void payTickets(UUID userId, List<UUID> ticketIds) {
        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        for (Ticket ticket : tickets) {
            if (!ticket.getUserId().equals(userId)) {
                throw new RuntimeException("Không tìm thấy vé hoặc vé không thuộc quyền sở hữu.");
            }
            if ("PAID".equals(ticket.getStatus())) {
                continue; // Already paid
            }
            ticket.setStatus("PAID");
            ticket.setUpdatedAt(OffsetDateTime.now());
            ticketRepository.save(ticket);
        }
    }

    public List<MovieRevenueDTO> getRevenueStats() {
        return ticketRepository.getRevenuePerMovie();
    }

    public List<ShowtimeRevenueDTO> getShowtimeStats() {
        return ticketRepository.getRevenuePerShowtime();
    }
}
