package com.lld.ashwinkumar.theatreticketbooking.providers;

import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;

import java.util.List;

public interface SeatLockProvider {

    void lockSeats(Show show, List<Seat> seats, String user);
    void unLockSeats(Show show, List<Seat> seats, String user);
    boolean validateLock(Show show, Seat seat, String user);
    List<Seat> getLockedSeats(Show show);

}
