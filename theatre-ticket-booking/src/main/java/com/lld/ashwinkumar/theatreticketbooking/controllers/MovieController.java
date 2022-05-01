package com.lld.ashwinkumar.theatreticketbooking.controllers;

import com.lld.ashwinkumar.theatreticketbooking.service.MovieService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MovieController {

    private MovieService service;

    public String createMovie(String movieName) {
        return service.createMovie(movieName).getId();
    }

}
