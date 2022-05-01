package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.exception.NotFoundException;
import com.lld.ashwinkumar.theatreticketbooking.models.Screen;
import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Theatre;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TheatreService {

    private Map<String, Theatre> theatres;
    private Map<String, Screen> screens;
    private Map<String, Seat> seats;

    public TheatreService() {
        this.theatres = new HashMap<>();
        this.screens = new HashMap<>();
        this.seats = new HashMap<>();
    }

    public Theatre createTheatre(String theatreName) {
        String id = UUID.randomUUID().toString();
        Theatre theatre = new Theatre(id, theatreName);
        this.theatres.put(id, theatre);
        return theatre;
    }

    public Screen createScreenInTheatre(Theatre theatre, String screenName) {
        Screen screen = createScreen(theatre, screenName);
        theatre.addScreen(screen);
        return screen;
    }

    private Screen createScreen(Theatre theatre, String screenName) {
        String id = UUID.randomUUID().toString();
        Screen screen = new Screen(id, screenName, theatre);
        this.screens.put(id, screen);
        return screen;
    }

    public Seat createSeatInScreen(Screen screen, String rowNumber, String seatNumber) {
        Seat seat = createSeat(rowNumber, seatNumber);
        screen.addSeat(seat);
        return seat;
    }

    private Seat createSeat(String rowNumber, String seatNumber) {
        String id = UUID.randomUUID().toString();
        Seat seat = new Seat(id, rowNumber, seatNumber);
        this.seats.put(id, seat);
        return seat;
    }

    public Theatre getTheatre(String theatreId) {
        if (!theatres.containsKey(theatreId)) {
            throw new NotFoundException();
        }
        return theatres.get(theatreId);
    }

    public Screen getScreen(String screenId) {
        if (!screens.containsKey(screenId)) {
            throw new NotFoundException();
        }
        return screens.get(screenId);
    }

    public Seat getSeat(String seatId) {
        if (!seats.containsKey(seatId)) {
            throw new NotFoundException();
        }
        return seats.get(seatId);
    }

}
