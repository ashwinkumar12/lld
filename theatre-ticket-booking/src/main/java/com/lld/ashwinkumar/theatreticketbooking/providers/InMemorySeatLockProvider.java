package com.lld.ashwinkumar.theatreticketbooking.providers;

import com.lld.ashwinkumar.theatreticketbooking.exception.SeatTemporarilyUnavailableException;
import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.SeatLock;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;

import java.util.*;
import java.util.stream.Collectors;

public class InMemorySeatLockProvider implements SeatLockProvider {

    private Integer timeout;
    private Map<Show, Map<Seat, SeatLock>> locks;

    public InMemorySeatLockProvider(Integer timeout, Map<Show, Map<Seat, SeatLock>> locks) {
        this.timeout = timeout;
        this.locks = new HashMap<>();
    }

    @Override
    synchronized public void lockSeats(Show show, List<Seat> seats, String user) {
        for (Seat seat : seats) {
            if (isSeatLocked(show, seat)) {
                throw new SeatTemporarilyUnavailableException();
            }
            lockSeat(show, seat, user, timeout);
        }

    }

    private void lockSeat(Show show, Seat seat, String user, Integer timeout) {
        if (!locks.containsKey(show)) {
            locks.put(show, new HashMap<>());
        }
        SeatLock lock = new SeatLock(show, seat, timeout, new Date(), user);
        locks.get(show).put(seat, lock);
    }

    private boolean isSeatLocked(Show show, Seat seat) {
        return locks.containsKey(show) && locks.get(show).containsKey(seat) && !locks.get(show).get(seat).isLockExpired();
    }

    @Override
    public void unLockSeats(Show show, List<Seat> seats, String user) {
        for (Seat seat : seats) {
            if (validateLock(show, seat, user)) {
                unLockSeat(show, seat, user);
            }
        }
    }

    private void unLockSeat(Show show, Seat seat, String user) {
        if (!locks.containsKey(show)) {
            return;
        }
        locks.get(show).remove(seat);
    }

    @Override
    public boolean validateLock(Show show, Seat seat, String user) {
        return isSeatLocked(show, seat) && locks.get(show).get(seat).getLockedBy().equals(user);
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        if (!locks.containsKey(show)) {
            return new ArrayList<Seat>();
        }
        return locks.get(show)
                .keySet()
                .stream()
                .filter(seat -> isSeatLocked(show, seat))
                .collect(Collectors.toList());
    }

}
