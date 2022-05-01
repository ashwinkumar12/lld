package com.lld.ashwinkumar.theatreticketbooking.controllers;

import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;
import com.lld.ashwinkumar.theatreticketbooking.service.BookingService;
import com.lld.ashwinkumar.theatreticketbooking.service.ShowService;
import com.lld.ashwinkumar.theatreticketbooking.service.TheatreService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookingController {

    private BookingService bookingService;
    private ShowService showService;
    private TheatreService theatreService;

    public String createBooking(String user, String showId, List<String> seatIds) {
        Show show = showService.getShow(showId);
        List<Seat> seats = seatIds.stream().map(seat -> theatreService.getSeat(seat)).collect(Collectors.toList());
        return bookingService.createBooking(user, show, seats).getId();
    }

}
