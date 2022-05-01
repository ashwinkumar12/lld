package com.lld.ashwinkumar.theatreticketbooking.service;

import com.lld.ashwinkumar.theatreticketbooking.exception.BadRequestException;
import com.lld.ashwinkumar.theatreticketbooking.models.Booking;
import com.lld.ashwinkumar.theatreticketbooking.providers.SeatLockProvider;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    private Map<Booking, Integer> bookingFailures;
    private Integer allowedRetries;
    private SeatLockProvider seatLockProvider;

    public PaymentService(Integer allowedRetries, SeatLockProvider seatLockProvider) {
        this.allowedRetries = allowedRetries;
        this.seatLockProvider = seatLockProvider;
        this.bookingFailures = new HashMap<>();
    }

    public void processPaymentFailures(Booking booking, String user) {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }
        if (!bookingFailures.containsKey(booking)) {
            bookingFailures.put(booking, 0);
        }
        Integer currentFailures = bookingFailures.get(booking);
        Integer newFailuresCount = currentFailures + 1;
        bookingFailures.put(booking, newFailuresCount);
        if (bookingFailures.get(booking) > allowedRetries) {
            seatLockProvider.unLockSeats(booking.getShow(), booking.getSeats(), user);
        }
    }

}
