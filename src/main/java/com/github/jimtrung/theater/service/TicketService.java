package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.BookingRequest;
import com.github.jimtrung.theater.model.Ticket;
import com.github.jimtrung.theater.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                throw new RuntimeException("Seat " + seatId + " is already booked.");
            }
            
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setShowtimeId(request.showtimeId());
            ticket.setSeatId(seatId);
            ticket.setPrice(50000); // Fixed price for now
            ticket.setCreatedAt(OffsetDateTime.now());
            ticket.setUpdatedAt(OffsetDateTime.now());
            
            tickets.add(ticketRepository.save(ticket));
        }
        
        return tickets;
    }

    public List<com.github.jimtrung.theater.dto.MovieRevenueDTO> getRevenueStats() {
        return ticketRepository.getRevenuePerMovie();
    }
}
