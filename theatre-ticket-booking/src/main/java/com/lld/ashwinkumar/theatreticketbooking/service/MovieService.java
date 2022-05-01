package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.exception.NotFoundException;
import com.lld.ashwinkumar.theatreticketbooking.models.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovieService {

    private Map<String, Movie> movies;

    public MovieService() {
        this.movies = new HashMap<>();
    }

    public Movie createMovie(String movieName) {
        String id = UUID.randomUUID().toString();
        Movie movie = new Movie(id, movieName);
        this.movies.put(id, movie);
        return movie;
    }

    public Movie getMovie(String movieId) {
        if (!movies.containsKey(movieId)) {
            throw new NotFoundException();
        }
        return movies.get(movieId);
    }

}
