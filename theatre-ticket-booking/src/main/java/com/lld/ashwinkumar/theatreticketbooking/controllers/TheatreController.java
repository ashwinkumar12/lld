package com.lld.ashwinkumar.theatreticketbooking.controllers;

import com.lld.ashwinkumar.theatreticketbooking.models.Screen;
import com.lld.ashwinkumar.theatreticketbooking.models.Theatre;
import com.lld.ashwinkumar.theatreticketbooking.service.TheatreService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TheatreController {

    private TheatreService service;

    public String createTheatre(String theatreName) {
        return service.createTheatre(theatreName).getId();
    }

    public String createScreen(String theatreId, String screenName) {
        Theatre theatre = service.getTheatre(theatreId);
        return service.createScreenInTheatre(theatre, screenName).getId();
    }

    public String createSeat(String screenId, String rowNumber, String seatNumber) {
        Screen screen = service.getScreen(screenId);
        return service.createSeatInScreen(screen, rowNumber, seatNumber).getId();
    }

}
