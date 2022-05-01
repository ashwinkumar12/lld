package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.exception.NotFoundException;
import com.lld.ashwinkumar.theatreticketbooking.exception.ScreenAlreadyOccupiedException;
import com.lld.ashwinkumar.theatreticketbooking.models.Movie;
import com.lld.ashwinkumar.theatreticketbooking.models.Screen;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;

import java.util.*;
import java.util.stream.Collectors;

public class ShowService {

    private Map<String, Show> shows;

    public ShowService() {
        this.shows = new HashMap<>();
    }

    public Show createShow(Movie movie, Screen screen, Date showStartTime, Integer duration) {
        if (! checkIfShowCreationAllowed(screen, showStartTime, duration)) {
            throw new ScreenAlreadyOccupiedException();
        }
        String id = UUID.randomUUID().toString();
        Show show = new Show(id, movie, screen, showStartTime, duration);
        this.shows.put(id, show);
        return show;
    }

    private boolean checkIfShowCreationAllowed(Screen screen, Date showStartTime, Integer duration) {
        //Implement logic
        return true;
    }

    public Show getShow(String showId) {
        if (! this.shows.containsKey(showId)) {
            throw new NotFoundException();
        }
        return this.shows.get(showId);
    }

    public List<Show> getShowsForScreen(Screen screen) {
        return shows.values().stream()
                .filter(show -> show.getScreen().getId().equals(screen.getId()))
                .collect(Collectors.toList());
    }

}
