package com.lld.ashwinkumar.theatreticketbooking.models;

import com.lld.ashwinkumar.theatreticketbooking.exception.InvalidStateException;
import lombok.Getter;

import java.util.List;

@Getter
public class Booking {

    private String id;
    private Show show;
    private List<Seat> seats;
    private String user;
    private BookingStatus status;

    public Booking(String id, Show show, List<Seat> seats, String user) {
        this.id = id;
        this.show = show;
        this.seats = seats;
        this.user = user;
        this.status = BookingStatus.CREATED;
    }

    public boolean isConfirmed() {
        return this.status == BookingStatus.CONFIRMED;
    }

    public void confirmBooking() {
        if (this.status != BookingStatus.CREATED) {
            throw new InvalidStateException();
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void expireBooking() {
        if (this.status != BookingStatus.CREATED) {
            throw new InvalidStateException();
        }
        this.status = BookingStatus.EXPIRED;
    }

}
