package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.Actor;
import com.github.jimtrung.theater.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actor")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Actor>> getAllActor() {
        return ResponseEntity.status(200).body(actorService.getAllActor());
    }
}
