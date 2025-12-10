package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.model.Movie;
import com.github.jimtrung.theater.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public void insertMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteAllMovies() {
        movieRepository.deleteAll();
    }

    public void deleteMovieById(UUID id) {
        movieRepository.deleteById(id);
    }

    public void updateMovie(UUID id, Movie movie) {
        movie.setId(id);
        movieRepository.save(movie);
    }
}
