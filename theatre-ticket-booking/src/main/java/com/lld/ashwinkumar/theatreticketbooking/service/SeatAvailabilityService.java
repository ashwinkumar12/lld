package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;
import com.lld.ashwinkumar.theatreticketbooking.providers.SeatLockProvider;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SeatAvailabilityService {

    private BookingService bookingService;
    private SeatLockProvider seatLockProvider;

    public List<Seat> getAvailableSeats(Show show) {
        List<Seat> allSeats = show.getScreen().getSeatList();
        List<Seat> unavailableSeats = getUnavailableSeats(show);

        List<Seat> availableSeats = new ArrayList<>(allSeats);
        availableSeats.removeAll(unavailableSeats);
        return availableSeats;
    }

    public List<Seat> getUnavailableSeats(Show show) {
        List<Seat> unavailableSeats = bookingService.getBookedSeats(show);
        List<Seat> tempUnavailableSeats = seatLockProvider.getLockedSeats(show);
        unavailableSeats.addAll(tempUnavailableSeats);
        return unavailableSeats;
    }

}
