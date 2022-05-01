package com.lld.ashwinkumar.theatreticketbooking.controllers;

import com.lld.ashwinkumar.theatreticketbooking.service.BookingService;
import com.lld.ashwinkumar.theatreticketbooking.service.PaymentService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentController {

    private BookingService bookingService;
    private PaymentService paymentService;

    public void paymentSuccess(String bookingId, String user) {
        bookingService.confirmBooking(bookingService.getBooking(bookingId), user);
    }

    public void paymentFailure(String bookingId, String user) {
        paymentService.processPaymentFailures(bookingService.getBooking(bookingId), user);
    }

}
