package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.BookingRequest;
import com.github.jimtrung.theater.model.Ticket;
import com.github.jimtrung.theater.service.TicketService;
import com.github.jimtrung.theater.service.UserService;
import com.github.jimtrung.theater.util.AuthTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final AuthTokenUtil authTokenUtil;

    public TicketController(TicketService ticketService, AuthTokenUtil authTokenUtil) {
        this.ticketService = ticketService;
        this.authTokenUtil = authTokenUtil;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookTickets(@RequestHeader("Authorization") String token, @RequestBody BookingRequest request) {
        try {
            String accessToken = token.substring(7);
            UUID userId = authTokenUtil.parseToken(accessToken);

            List<Ticket> tickets = ticketService.bookTickets(userId, request);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/stats/showtime")
    public ResponseEntity<?> getShowtimeStats() {
        return ResponseEntity.ok(ticketService.getShowtimeStats());
    }
}
