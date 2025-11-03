package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.Director;
import com.github.jimtrung.theater.service.DirectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/director")
public class DirectorController {
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Director>> getAllDirector() {
        return ResponseEntity.status(200).body(directorService.getAllDirector());
    }
}
