package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.MovieActorsRequest;
import com.github.jimtrung.theater.model.MovieActor;
import com.github.jimtrung.theater.service.MovieActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-actors")
public class MovieActorController {
    private final MovieActorService movieActorService;

    public MovieActorController(MovieActorService movieActorService) {
        this.movieActorService = movieActorService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addMovieActors(@RequestBody MovieActorsRequest request) {
        request.actorsId().forEach(actorId -> {
            movieActorService.insertMovieActors( new MovieActor(request.movieId(), actorId) );
        });

        return ResponseEntity.ok().body("Add actors to movie successfully");
    }
}
