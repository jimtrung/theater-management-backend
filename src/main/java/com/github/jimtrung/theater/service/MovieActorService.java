package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.MovieActorsDAO;
import com.github.jimtrung.theater.model.MovieActor;
import org.springframework.stereotype.Service;

@Service
public class MovieActorService {
    private final MovieActorsDAO movieActorsDAO;

    public MovieActorService(MovieActorsDAO movieActorsDAO) {
        this.movieActorsDAO = movieActorsDAO;
    }

    public void insertMovieActors(MovieActor ma) {
        movieActorsDAO.insert(ma);
    }
}
