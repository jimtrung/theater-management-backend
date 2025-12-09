package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    private final TicketService ticketService;

    public StatisticsController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {
        return ResponseEntity.ok(ticketService.getRevenueStats());
    }
}
