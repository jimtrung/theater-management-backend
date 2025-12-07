package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.model.Auditorium;
import com.github.jimtrung.theater.repository.AuditoriumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;

    public AuditoriumService(AuditoriumRepository auditoriumRepository) {
        this.auditoriumRepository = auditoriumRepository;
    }

    public List<Auditorium> getAllAuditoriums() {
        return auditoriumRepository.findAll();
    }

    public void insert(Auditorium auditorium) {
        auditoriumRepository.save(auditorium);
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
