package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.exception.BadRequestException;
import com.lld.ashwinkumar.theatreticketbooking.exception.NotFoundException;
import com.lld.ashwinkumar.theatreticketbooking.exception.SeatPermanentlyUnavailableException;
import com.lld.ashwinkumar.theatreticketbooking.models.Booking;
import com.lld.ashwinkumar.theatreticketbooking.models.Seat;
import com.lld.ashwinkumar.theatreticketbooking.models.Show;
import com.lld.ashwinkumar.theatreticketbooking.providers.SeatLockProvider;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {

    private Map<String, Booking> bookings;
    private SeatLockProvider seatLockProvider;

    public BookingService(Map<String, Booking> bookings, SeatLockProvider seatLockProvider) {
        this.bookings = new HashMap<>();
        this.seatLockProvider = seatLockProvider;
    }

    public Booking getBooking(String bookingId) {
        if (!bookings.containsKey(bookingId)) {
            throw new NotFoundException();
        }
        return bookings.get(bookingId);
    }

    public List<Booking> getAllBookings(Show show) {
        return bookings.values().stream().filter(booking -> booking.getShow().equals(show)).collect(Collectors.toList());
    }

    public Booking createBooking(String user, Show show, List<Seat> seats) {
        if (isAnySeatAlreadyBooked(show, seats)) {
            throw new SeatPermanentlyUnavailableException();
        }
        seatLockProvider.lockSeats(show, seats, user);
        String id = UUID.randomUUID().toString();
        Booking newBooking = new Booking(id, show, seats, user);
        bookings.put(id, newBooking);
        return newBooking;
    }

    private boolean isAnySeatAlreadyBooked(Show show, List<Seat> seats) {
        List<Seat> bookedSeats = getBookedSeats(show);
        for (Seat seat : seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    public List<Seat> getBookedSeats(Show show) {
        return getAllBookings(show).stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeats)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void confirmBooking(Booking booking, String user) {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }

        for (Seat seat : booking.getSeats()) {
            if (!seatLockProvider.validateLock(booking.getShow(), seat, user)) {
                throw new BadRequestException();
            }
        }

        booking.confirmBooking();
    }

}
