package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.model.Auditorium;
import com.github.jimtrung.theater.model.Seat;
import com.github.jimtrung.theater.repository.AuditoriumRepository;
import com.github.jimtrung.theater.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import java.util.List;
import java.util.UUID;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;

    public AuditoriumService(AuditoriumRepository auditoriumRepository, SeatRepository seatRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.seatRepository = seatRepository;
    }

    public List<Auditorium> getAllAuditoriums() {
        return auditoriumRepository.findAll();
    }

    public void insert(Auditorium auditorium) {
        Auditorium savedAuditorium = auditoriumRepository.save(auditorium);
        generateSeats(savedAuditorium);
    }

    private void generateSeats(Auditorium auditorium) {
        int capacity = auditorium.getCapacity();
        int columns = 10;
        int rows = (int) Math.ceil((double) capacity / columns);

        // TODO: This should be async
        for (int i = 0; i < rows; i++) {
            String rowLabel = String.valueOf((char) ('A' + i));
            for (int j = 1; j <= columns; j++) {
                if (capacity <= 0) break;

                Seat seat = new Seat();
                seat.setAuditoriumId(auditorium.getId());
                seat.setRow(rowLabel);
                seat.setNumber(j);
                seat.setCreatedAt(OffsetDateTime.now());
                seat.setUpdatedAt(OffsetDateTime.now());

                seatRepository.save(seat);
                capacity--;
            }
        }
    }

    public void deleteAllAuditoriums() {
        auditoriumRepository.deleteAll();
    }

    public void delete(UUID id) {
        auditoriumRepository.deleteById(id);
    }

    public Auditorium getAuditoriumById(UUID id) {
        return auditoriumRepository.findById(id).orElse(new Auditorium());
    }

    public void updateAuditoriumById(UUID id, Auditorium auditorium) {
        auditorium.setId(id);
        auditoriumRepository.save(auditorium);
    }
}
