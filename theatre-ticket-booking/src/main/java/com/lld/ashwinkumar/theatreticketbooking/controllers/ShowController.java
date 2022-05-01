package com.lld.ashwinkumar.theatreticketbooking.controllers;

import com.lld.ashwinkumar.theatreticketbooking.models.Movie;
import com.lld.ashwinkumar.theatreticketbooking.models.Screen;
import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;
import com.lld.ashwinkumar.theatreticketbooking.service.MovieService;
import com.lld.ashwinkumar.theatreticketbooking.service.SeatAvailabilityService;
import com.lld.ashwinkumar.theatreticketbooking.service.ShowService;
import com.lld.ashwinkumar.theatreticketbooking.service.TheatreService;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowController {

    private ShowService showService;
    private MovieService movieService;
    private TheatreService theatreService;
    private SeatAvailabilityService seatAvailabilityService;

    public String createShow(String movieId, String screenId, Date startTime, Integer durationInSeconds) {
        Screen screen = theatreService.getScreen(screenId);
        Movie movie = movieService.getMovie(movieId);
        return showService.createShow(movie, screen, startTime, durationInSeconds).getId();
    }

    public List<String> getAvailableSeats(String showId) {
        Show show = showService.getShow(showId);
        List<Seat> availableSeats = seatAvailabilityService.getAvailableSeats(show);
        return availableSeats.stream().map(Seat::getId).collect(Collectors.toList());
    }

}
